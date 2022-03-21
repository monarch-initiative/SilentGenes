package org.monarchinitiative.sgenes.jannovar;

import de.charite.compbio.jannovar.data.JannovarData;
import de.charite.compbio.jannovar.data.ReferenceDictionary;
import de.charite.compbio.jannovar.reference.GenomeInterval;
import de.charite.compbio.jannovar.reference.TranscriptModel;
import org.monarchinitiative.sgenes.model.*;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class JannovarIterator implements Iterator<Gene> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JannovarIterator.class);

    // Jannovar stores coordinates in 0-based system
    private static final CoordinateSystem COORDINATE_SYSTEM = CoordinateSystem.zeroBased();
    private static final Pattern REFSEQ_TX = Pattern.compile("(?<prefix>[NX][MR])_(?<value>\\d+)\\.(?<version>\\d+)");
    private static final Pattern ENSEMBL_TX = Pattern.compile("(?<prefix>ENST)(?<value>\\d{11})\\.(?<version>\\d+)");
    private static final Pattern UCSC_TX = Pattern.compile("(?<prefix>uc)(?<value>\\d{3})(?<suffix>\\w{3})\\.(?<version>\\d+)");

    private final ReferenceDictionary rd;
    private final Iterator<Map.Entry<String, Collection<TranscriptModel>>> iterator;
    private final GenomicAssembly assembly;

    private final Queue<Gene> genes = new LinkedList<>();

    JannovarIterator(JannovarData jannovarData, GenomicAssembly assembly) {
        this.assembly = assembly;
        this.rd = jannovarData.getRefDict();
        Map<String, Collection<TranscriptModel>> geneMap = jannovarData.getTmByGeneSymbol().asMap();
        iterator = geneMap.entrySet().iterator();
        readNextGenes();
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

    private static Optional<GenomicRegion> parseLocation(Contig contig, Collection<TranscriptModel> transcripts) {
        int start = contig.length(), end = 0;

        if (transcripts.isEmpty())
            return Optional.empty();

        Strand st = null;
        for (TranscriptModel tx : transcripts) {
            GenomeInterval txRegion = tx.getTXRegion();
            start = Math.min(start, txRegion.getBeginPos());
            end = Math.max(end, txRegion.getEndPos());
            Strand currentStrand = parseStrand(tx.getStrand());
            if (st == null) {
                st = currentStrand;
            } else {
                if (!st.equals(currentStrand)) {
                    return Optional.empty();
                }
            }
        }

        return Optional.of(GenomicRegion.of(contig, st, COORDINATE_SYSTEM, start, end));
    }

    /**
     * We parse a collection of transcripts into more than one gene because of the genes that are in the pseudo-autosomal
     * regions
     */
    private static Collection<Gene> parseNextGene(GenomicAssembly assembly,
                                                  ReferenceDictionary rd,
                                                  String hgvsGeneSymbol,
                                                  Collection<TranscriptModel> transcripts) {
        Map<Integer, List<TranscriptModel>> txsByContig = transcripts.stream()
                .collect(Collectors.groupingBy(TranscriptModel::getChr));

        List<Gene> genes = new ArrayList<>(txsByContig.size());
        for (Map.Entry<Integer, List<TranscriptModel>> entry : txsByContig.entrySet()) {
            Integer contigId = entry.getKey();
            List<TranscriptModel> txs = entry.getValue();

            Optional<GeneIdentifier> id = createGeneIdentifier(hgvsGeneSymbol, txs);
            if (id.isEmpty()) {
                LOGGER.debug("No proper gene accession is available for gene `{}`", hgvsGeneSymbol);
                continue;
            }

            String contigName = rd.getContigIDToName().get(contigId);
            Contig contig = assembly.contigByName(contigName);
            if (contig.isUnknown()) {
                LOGGER.debug("Unknown contig {}({}) for gene `{}`", contigName, contigId, hgvsGeneSymbol);
                continue;
            }

            Optional<GenomicRegion> location = parseLocation(contig, txs);
            if (location.isEmpty()) {
                LOGGER.debug("Transcripts of {} are located on multiple strands", hgvsGeneSymbol);
                continue;
            }
            List<? extends Transcript> parsedTxs = parseTranscripts(contig, txs);

            Gene gene = Gene.of(id.get(), location.get(), parsedTxs);
            genes.add(gene);
        }

        return genes;
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

    private static List<? extends Transcript> parseTranscripts(Contig contig, Collection<TranscriptModel> transcripts) {
        List<Transcript> txs = new ArrayList<>(transcripts.size());

        for (TranscriptModel tx : transcripts) {
            // We use accession as transcript symbol. CCDS info is present as e.g. `CCDS75928|CCDS6966`, and it is impossible to map it to the respective transcripts
            Strand strand = parseStrand(tx.getStrand());
            TranscriptIdentifier txId = TranscriptIdentifier.of(tx.getAccession(), tx.getAccession(), null);
            TranscriptMetadata metadata = TranscriptMetadata.of(parseTranscriptEvidence(tx.getAccession(), tx.getTranscriptSupportLevel()));
            if (tx.isCoding()) {
                txs.add(parseCodingTranscript(contig, strand, txId, metadata, tx));
            } else {
                txs.add(parseNoncodingTranscript(contig, strand, txId, metadata, tx));
            }
        }

        return Collections.unmodifiableList(txs);
    }

    private static TranscriptEvidence parseTranscriptEvidence(String accession, int transcriptSupportLevel) {
        Matcher refseqMatcher = REFSEQ_TX.matcher(accession);
        if (refseqMatcher.matches()) {
            String prefix = refseqMatcher.group("prefix");
            switch (prefix.toUpperCase()) {
                case "NM":
                case "NR":
                    return TranscriptEvidence.KNOWN;
                case "XM":
                case "XR":
                    return TranscriptEvidence.MODEL;
            }
        }

        Matcher ensemblMatcher = ENSEMBL_TX.matcher(accession);
        if (ensemblMatcher.matches()) {
            // There is currently no way to deduce TranscriptEvidence from the accession. We can use TSL though
            return parseTranscriptSupportLevel(transcriptSupportLevel);
        }

        Matcher ucscMatcher = UCSC_TX.matcher(accession);
        if (ucscMatcher.matches()) {
            return parseTranscriptSupportLevel(transcriptSupportLevel);
        }

        return null;
    }

    private static TranscriptEvidence parseTranscriptSupportLevel(int transcriptSupportLevel) {
        switch (transcriptSupportLevel) {
            case -1:
                /*
                 * the transcript was not analyzed for one of the following reasons:
                 *
                 * <ul>
                 * <li>pseudogene annotation, including transcribed pseudogenes</li>
                 * <li>human leukocyte antigen (HLA) transcript</li>
                 * <li>immunoglobin gene transcript</li>
                 * <li>T-cell receptor transcript</li>
                 * <li>single-exon transcript (will be included in a future version)</li>
                 * </ul>
                 */
                return null;
            case 6:
                // Annotated as canonical transcript by UCSC (used in absence of TSL).
                return TranscriptEvidence.CANONICAL;
            case 8:
                // Lowest available priority (used in absence of any TSL and UCSC annotation of this transcript).
                return null;
            default:
                LOGGER.warn("Unexpected transcript support level {}", transcriptSupportLevel);
                return null;
        }
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

    private void readNextGenes() {
        while (iterator.hasNext() && genes.isEmpty()) {
            // Conversion of Jannovar gene can fail and parseNext can return null. Let's make sure this method only
            // returns null when there are no more proper genes left in the cache.
            Map.Entry<String, Collection<TranscriptModel>> entry = iterator.next();
            genes.addAll(parseNextGene(assembly, rd, entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public boolean hasNext() {
        return !genes.isEmpty();
    }

    @Override
    public Gene next() {
        Gene current = genes.poll();
        if (genes.isEmpty())
            readNextGenes();

        return current;
    }

}
