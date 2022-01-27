package xyz.ielis.silent.genes.jannovar;

import de.charite.compbio.jannovar.data.JannovarData;
import de.charite.compbio.jannovar.reference.GenomeInterval;
import de.charite.compbio.jannovar.reference.TranscriptModel;
import org.monarchinitiative.svart.*;
import xyz.ielis.silent.genes.model.*;

import java.util.*;

public class JannovarIterator implements Iterator<Gene> {

    // Jannovar stores coordinates in 0-based system
    private static final CoordinateSystem COORDINATE_SYSTEM = CoordinateSystem.zeroBased();
    private static final String NCBI_GENE_ID_IS_NA = null; // not available in Jannovar databases
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
        Map<String, String> altGeneIds = first.getAltGeneIDs();
        String geneId = altGeneIds.get("ENSEMBL_GENE_ID");
        if (geneId == null)
            geneId = altGeneIds.get("HGNC_ID");
        if (geneId == null)
            return null; // no proper gene accession is available

        String symbol = entry.getKey();

        GeneIdentifier id = GeneIdentifier.of(geneId, symbol, altGeneIds.get("HGNC_ID"), NCBI_GENE_ID_IS_NA);

        GenomeInterval txInterval = first.getTXRegion();
        String contigName = txInterval.getRefDict().getContigIDToName().get(txInterval.getChr());
        Contig contig = assembly.contigByName(contigName);
        if (contig.isUnknown())
            return null; // unknown contig

        Strand strand = parseStrand(txInterval.getStrand());
        GenomicRegion location = parseLocation(contig, strand, transcripts);
        List<? extends Transcript> txs = parseTranscripts(contig, strand, transcripts);

        return Gene.of(id, location, txs);
    }

    private static List<? extends Transcript> parseTranscripts(Contig contig, Strand strand, Collection<TranscriptModel> transcripts) {
        List<Transcript> txs = new ArrayList<>(transcripts.size());

        for (TranscriptModel tx : transcripts) {
            // Symbol is not transferred, nor is ccdsId
            TranscriptIdentifier txId = TranscriptIdentifier.of(tx.getAccession(), "", null);
            if (tx.isCoding()) {
                txs.add(parseCodingTranscript(contig, strand, txId, tx));
            } else {
                txs.add(parseNoncodingTranscript(contig, strand, txId, tx));
            }
        }

        return Collections.unmodifiableList(txs);
    }

    private static CodingTranscript parseCodingTranscript(Contig contig, Strand strand, TranscriptIdentifier txId, TranscriptModel tx) {
        GenomeInterval txRegion = tx.getTXRegion();
        GenomicRegion location = GenomicRegion.of(contig, strand, COORDINATE_SYSTEM, txRegion.getBeginPos(), txRegion.getEndPos());
        List<Coordinates> exons = remapExons(tx.getExonRegions());
        GenomeInterval cds = tx.getCDSRegion();
        Coordinates cdsCoordinates = Coordinates.of(COORDINATE_SYSTEM, cds.getBeginPos(), cds.getEndPos());
//        Coordinates stopCodon = Coordinates.of(COORDINATE_SYSTEM, cds.getEndPos() - 3, cds.getEndPos());
//        Coordinates fivePrimeRegion = Coordinates.of(COORDINATE_SYSTEM, txRegion.getBeginPos(), cds.getBeginPos() - 1);
//        Coordinates threePrimeRegion = Coordinates.of(COORDINATE_SYSTEM, cds.getEndPos() + 2, txRegion.getEndPos());

        return Transcript.coding(txId, location, exons, cdsCoordinates);
    }

    private static Transcript parseNoncodingTranscript(Contig contig, Strand strand, TranscriptIdentifier txId, TranscriptModel tx) {

        GenomeInterval txRegion = tx.getTXRegion();
        GenomicRegion location = GenomicRegion.of(contig, strand, COORDINATE_SYSTEM, txRegion.getBeginPos(), txRegion.getEndPos());
        List<Coordinates> exons = remapExons(tx.getExonRegions());

        return Transcript.noncoding(txId, location, exons);
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
