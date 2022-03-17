package org.monarchinitiative.sgenes.jannovar;

import de.charite.compbio.jannovar.data.JannovarData;
import de.charite.compbio.jannovar.reference.GenomeInterval;
import de.charite.compbio.jannovar.reference.TranscriptModel;
import org.monarchinitiative.sgenes.model.*;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.*;

import java.util.*;

class JannovarIterator implements Iterator<Gene> {

    // Jannovar stores coordinates in 0-based system
    private static final CoordinateSystem COORDINATE_SYSTEM = CoordinateSystem.zeroBased();
    private final Iterator<Map.Entry<String, Collection<TranscriptModel>>> iterator;
    private final GenomicAssembly assembly;

    private Gene next;

    JannovarIterator(JannovarData jannovarData, GenomicAssembly assembly) {
        this.assembly = assembly;
        Map<String, Collection<TranscriptModel>> geneMap = jannovarData.getTmByGeneSymbol().asMap();
        iterator = geneMap.entrySet().iterator();
        next = parseGene();
    }

    private static Strand parseStrand(de.charite.compbio.jannovar.reference.Strand strand) {
        switch (strand) {
            case FWD:
                return Strand.POSITIVE;
            case REV:
                return Strand.NEGATIVE;
            default:
                throw new IllegalArgumentException(String.format("Unknown strand %s", strand));
        }
    }

    private static GenomicRegion parseLocation(Contig contig, Strand strand, Collection<TranscriptModel> transcripts) {
        int start = contig.length(), end = 0;

        for (TranscriptModel tx : transcripts) {
            GenomeInterval txRegion = tx.getTXRegion();
            start = Math.min(start, txRegion.getBeginPos());
            end = Math.max(end, txRegion.getEndPos());
        }

        return GenomicRegion.of(contig, strand, COORDINATE_SYSTEM, start, end);
    }

    private static Gene parseNextGene(GenomicAssembly assembly, Map.Entry<String, Collection<TranscriptModel>> entry) {
        Collection<TranscriptModel> transcripts = entry.getValue();
        if (transcripts.isEmpty())
            return null; // no transcripts defined for the gene

        TranscriptModel first = transcripts.iterator().next();
        Optional<GeneIdentifier> id = createGeneIdentifier(entry.getKey(), transcripts);
        if (id.isEmpty())
            return null; // no proper gene accession is available

        GenomeInterval txInterval = first.getTXRegion();
        String contigName = txInterval.getRefDict().getContigIDToName().get(txInterval.getChr());
        Contig contig = assembly.contigByName(contigName);
        if (contig.isUnknown())
            return null; // unknown contig

        Strand strand = parseStrand(txInterval.getStrand());
        GenomicRegion location = parseLocation(contig, strand, transcripts);
        List<? extends Transcript> txs = parseTranscripts(contig, strand, transcripts);

        return Gene.of(id.get(), location, txs);
    }

    private static Optional<GeneIdentifier> createGeneIdentifier(String symbol, Collection<TranscriptModel> transcripts) {
        String geneId = null, hgncId = null, ncbiGeneId = null;
        for (TranscriptModel tx : transcripts) {
            Map<String, String> altGeneIDs = tx.getAltGeneIDs();

            // 1 - geneId
            if (geneId == null) {
                geneId = altGeneIDs.get("ENSEMBL_GENE_ID");
                if (geneId == null)
                    geneId = altGeneIDs.get("HGNC_ID");
            }

            // 2 - hgncId
            if (hgncId == null)
                hgncId = altGeneIDs.get("HGNC_ID");

            // 3 - ncbiGeneId
            if (ncbiGeneId == null) {
                String entrezId = altGeneIDs.get("ENTREZ_ID"); // NCBIGene is numerically identical to ENTREZ id
                ncbiGeneId = entrezId == null ? null : "NCBIGene:" + entrezId;
            }

            // ------------------------------------------------------------------------------------------------------ //
            if (geneId != null && hgncId != null && ncbiGeneId != null)
                return Optional.of(GeneIdentifier.of(geneId, symbol, hgncId, ncbiGeneId));
        }
        return geneId == null // Mandatory field is missing. Symbol should always be present
                ? Optional.empty()
                : Optional.of(GeneIdentifier.of(geneId, symbol, hgncId, ncbiGeneId));
    }

    private static List<? extends Transcript> parseTranscripts(Contig contig, Strand strand, Collection<TranscriptModel> transcripts) {
        List<Transcript> txs = new ArrayList<>(transcripts.size());

        for (TranscriptModel tx : transcripts) {
            // Symbol is not transferred, nor is ccdsId
            TranscriptIdentifier txId = TranscriptIdentifier.of(tx.getAccession(), "", null);
            TranscriptMetadata metadata = TranscriptMetadata.of(parseTranscriptSupportLevel(tx.getTranscriptSupportLevel()));
            if (tx.isCoding()) {
                txs.add(parseCodingTranscript(contig, strand, txId, metadata, tx));
            } else {
                txs.add(parseNoncodingTranscript(contig, strand, txId, metadata, tx));
            }
        }

        return Collections.unmodifiableList(txs);
    }

    private static TranscriptEvidence parseTranscriptSupportLevel(int transcriptSupportLevel) {
        // TODO - implement a proper parse
        return null;
    }

    private static Transcript parseCodingTranscript(Contig contig, Strand strand, TranscriptIdentifier txId, TranscriptMetadata metadata, TranscriptModel tx) {
        GenomeInterval txRegion = tx.getTXRegion();
        GenomicRegion location = GenomicRegion.of(contig, strand, COORDINATE_SYSTEM, txRegion.getBeginPos(), txRegion.getEndPos());
        List<Coordinates> exons = remapExons(tx.getExonRegions());
        GenomeInterval cds = tx.getCDSRegion();
        Coordinates cdsCoordinates = Coordinates.of(COORDINATE_SYSTEM, cds.getBeginPos(), cds.getEndPos());

        return Transcript.of(txId, location, exons, cdsCoordinates, metadata);
    }

    private static Transcript parseNoncodingTranscript(Contig contig, Strand strand, TranscriptIdentifier txId, TranscriptMetadata metadata, TranscriptModel tx) {

        GenomeInterval txRegion = tx.getTXRegion();
        GenomicRegion location = GenomicRegion.of(contig, strand, COORDINATE_SYSTEM, txRegion.getBeginPos(), txRegion.getEndPos());
        List<Coordinates> exons = remapExons(tx.getExonRegions());

        return Transcript.of(txId, location, exons, null, metadata);
    }

    private static List<Coordinates> remapExons(List<GenomeInterval> exonRegions) {
        List<Coordinates> exons = new ArrayList<>(exonRegions.size());
        for (GenomeInterval exon : exonRegions) {
            exons.add(Coordinates.of(COORDINATE_SYSTEM, exon.getBeginPos(), exon.getEndPos()));
        }
        return exons;
    }

    private Gene parseGene() {
        Gene gene = null;
        while (iterator.hasNext() && gene == null) {
            // Conversion of Jannovar gene can fail and parseNext can return null. Let's make sure this method only
            // returns null when there are no more proper genes left in the cache.
            gene = parseNextGene(assembly, iterator.next());
        }
        return gene;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public Gene next() {
        Gene current = next;
        next = parseGene();
        return current;
    }

}
