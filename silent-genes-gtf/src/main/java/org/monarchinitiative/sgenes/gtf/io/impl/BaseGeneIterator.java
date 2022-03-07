package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.io.gtf.GtfRecord;
import org.monarchinitiative.sgenes.gtf.io.gtf.GtfRecordParser;
import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.assembly.SequenceRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * The base GTF iterator that iterates through a GTF file and assembles {@link GENE}s from a list of {@link GtfRecord}s.
 * The inheritor is required to implement {@link #assembleGene(String, List)}.
 *
 * @param <GENE>     gene type
 * @param <METADATA> container for gene-related metadata
 */
abstract class BaseGeneIterator<GENE extends Gene, METADATA, TX extends Transcript> implements Iterator<GENE> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseGeneIterator.class);
    // Each transcript/exon must have these attributes, no matter what. Otherwise, we cannot partition Gtf lines.
    private static final Set<String> MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES = Set.of("gene_id", "transcript_id");
    private static final Set<String> MANDATORY_EXON_ATTRIBUTE_NAMES = Set.of("gene_id", "transcript_id");
    // GTF line has exon number attribute stored under this key
    private static final String EXON_NUMBER_ATTRIBUTE_KEY = "exon_number";

    protected final GenomicAssembly genomicAssembly;
    private final Queue<GENE> queue = new LinkedList<>();
    private final Set<String> mandatoryTranscriptAttributes;
    private final Set<String> mandatoryExonAttributes;
    protected BufferedReader reader;
    private String currentContig;
    private GtfRecord firstRecordOfNextContig;

    protected BaseGeneIterator(Path gencodeGtfPath,
                               GenomicAssembly genomicAssembly,
                               Set<String> mandatoryTranscriptAttributes,
                               Set<String> mandatoryExonAttributes) {
        this.genomicAssembly = genomicAssembly;
        this.mandatoryTranscriptAttributes = setUnion(mandatoryTranscriptAttributes, MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES);
        this.mandatoryExonAttributes = setUnion(mandatoryExonAttributes, MANDATORY_EXON_ATTRIBUTE_NAMES);
        try {
            reader = openForReading(gencodeGtfPath);
            readNextContig();
        } catch (IOException e) {
            LOGGER.warn("Error opening GTF at `{}`: {}", gencodeGtfPath, e.getMessage());
        }
    }

    private static Set<String> setUnion(Set<String> left, Set<String> right) {
        Set<String> union = new HashSet<>(left);
        union.addAll(right);
        return union;
    }

    /**
     * Transform the lines of records located on the next contig into {@link GtfRecord}s, and convert the records
     * to {@link GencodeGene}s.
     */
    private void readNextContig() {
        List<GtfRecord> records = readPrimaryContigAndSubsequentNonPrimaryContigRecords();

        Map<String, List<GtfRecord>> recordByGeneId = records.stream()
                .collect(Collectors.groupingBy(GtfRecord::geneId, Collectors.toUnmodifiableList()));

        recordByGeneId.entrySet().stream()
                .map(e -> assembleGene(e.getKey(), e.getValue()))
                .flatMap(Optional::stream)
                .forEach(queue::add);
    }

    /**
     * Read the records of the next contig ({@link SequenceRole#ASSEMBLED_MOLECULE}) and the records of the following
     * contigs that are not {@link SequenceRole#ASSEMBLED_MOLECULE}s.
     * <p>
     * Some non-primary contigs contain no genes which can stall the contig-based iteration.
     */
    private List<GtfRecord> readPrimaryContigAndSubsequentNonPrimaryContigRecords() {
        List<GtfRecord> contigRecords = new ArrayList<>();
        try {
            if (firstRecordOfNextContig != null) {
                // the first record is null before we process the first contig
                contigRecords.add(firstRecordOfNextContig);
                // set to null so that the last row is not added into the contigRecords list after processing all contigs
                firstRecordOfNextContig = null;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) continue; // header

                Optional<GtfRecord> gtfRecord = GtfRecordParser.parseLine(line, genomicAssembly);
                if (gtfRecord.isPresent()) {
                    GtfRecord record = gtfRecord.get();
                    if (currentContig == null)
                        currentContig = record.contigName();

                    if (currentContig.equals(record.contigName()) || !record.contig().sequenceRole().equals(SequenceRole.ASSEMBLED_MOLECULE)) {
                        // still processing the current contig
                        contigRecords.add(record);
                    } else {
                        // the first line of the next contig
                        currentContig = record.contigName();
                        firstRecordOfNextContig = record;
                        break;
                    }
                }

            }
        } catch (IOException e) {
            LOGGER.warn("Error occurred during reading GTF file: {}", e.getMessage(), e);
            contigRecords.clear();
        }
        return contigRecords;
    }

    /**
     * Assemble a {@link GENE} instance with <code>geneId</code> from a list of <code>records</code>.
     *
     * @param geneId  gene ID
     * @param records list of GTF records that belong to the {@link GENE} instance
     * @return optional with {@link GENE} if the assembly is possible, or an empty optional
     */
    private Optional<GENE> assembleGene(String geneId, List<GtfRecord> records) {
        Optional<GtfGeneData> optional = partitionGtfLines(geneId, records);
        if (optional.isEmpty())
            return Optional.empty();

        GtfGeneData gtfGeneData = optional.get();

        // gene identifier
        GtfRecord gene = gtfGeneData.gene();
        if (gene == null) {
            LOGGER.warn("Did not find gene GTF row for gene {}", geneId);
            return Optional.empty();
        }
        Optional<GeneIdentifier> geneIdentifier = parseGeneIdentifier(geneId, gene);
        if (geneIdentifier.isEmpty())
            return Optional.empty();

        // metadata
        Optional<METADATA> metadata = parseGeneMetadata(geneId, gene);

        if (metadata.isEmpty())
            return Optional.empty();

        // transcripts
        List<GtfRecord> transcripts = gtfGeneData.transcripts();
        Map<String, List<GtfRecord>> exons = gtfGeneData.exonsByTxId();
        Map<String, GtfRecord> startCodons = gtfGeneData.startCodonByTxId();
        Map<String, GtfRecord> stopCodons = gtfGeneData.stopCodonByTxId();

        List<TX> txs = new ArrayList<>(transcripts.size());
        for (GtfRecord txRecord : transcripts) {
            String txId = txRecord.firstAttribute("transcript_id"); // the attribute is present (checked above)
            List<GtfRecord> txExons = exons.get(txId);
            GtfRecord startCodon = startCodons.get(txId);
            GtfRecord stopCodon = stopCodons.get(txId);
            Optional<TX> transcript = processTranscript(txId, txRecord, txExons, startCodon, stopCodon);
            transcript.ifPresent(txs::add);
        }
        if (txs.isEmpty()) {
            LOGGER.warn("No transcripts could be parsed for gene `{}`", geneId);
            return Optional.empty();
        }

        return Optional.of(newGeneInstance(geneIdentifier.get(), gene.location(), txs, metadata.get()));
    }

    protected abstract Optional<GeneIdentifier> parseGeneIdentifier(String geneId, GtfRecord gene);

    protected abstract Optional<METADATA> parseGeneMetadata(String geneId, GtfRecord gene);

    protected abstract Optional<TX> processTranscript(String txId,
                                                      GtfRecord tx,
                                                      List<GtfRecord> exonRecords,
                                                      GtfRecord startCodon,
                                                      GtfRecord stopCodon);

    protected abstract GENE newGeneInstance(GeneIdentifier geneIdentifier,
                                            GenomicRegion location,
                                            List<TX> transcripts,
                                            METADATA geneMetadata);

    protected Optional<GtfGeneData> partitionGtfLines(String geneId, List<GtfRecord> records) {
        GtfRecord gene = null;
        List<GtfRecord> transcripts = new LinkedList<>();
        Map<String, List<GtfRecord>> exons = new HashMap<>();
        Map<String, GtfRecord> startCodons = new HashMap<>();
        Map<String, GtfRecord> stopCodons = new HashMap<>();
        // partition the GTF records
        for (GtfRecord record : records) {
            switch (record.feature()) {
                case GENE:
                    if (gene != null) {
                        LOGGER.warn("2nd gene record was seen for gene {}: `{}`", geneId, record);
                        return Optional.empty();
                    }
                    gene = record;
                    break;
                case TRANSCRIPT:
                    // "transcript_id", "transcript_type", "transcript_name", "level"
                    if (!record.attributes().containsAll(mandatoryTranscriptAttributes)) {
                        return reportMissingAttributesAndReturn(record, mandatoryTranscriptAttributes);
                    }
                    transcripts.add(record);
                    break;
                case EXON:
                    // "transcript_id", "exon_id", "exon_number"
                    if (!record.attributes().containsAll(mandatoryExonAttributes)) {
                        return reportMissingAttributesAndReturn(record, mandatoryExonAttributes);
                    }
                    String exonTxId = record.firstAttribute("transcript_id");
                    exons.putIfAbsent(exonTxId, new ArrayList<>());
                    exons.get(exonTxId).add(record);
                    break;
                case START_CODON:
                    String startCodonTxId = record.firstAttribute("transcript_id");
                    if (startCodonTxId == null) {
                        LOGGER.warn("Missing `transcript_id` in start codon record for {}: `{}`", geneId, record);
                        break;
                    }
                    startCodons.put(startCodonTxId, record);
                    break;
                case STOP_CODON:
                    String stopCodonTxId = record.firstAttribute("transcript_id");
                    if (stopCodonTxId == null) {
                        LOGGER.warn("Missing `transcript_id` in stop codon record for {}: `{}`", geneId, record);
                        break;
                    }
                    stopCodons.put(stopCodonTxId, record);
                    break;
                default:
                    break;
            }
        }

        if (gene == null) {
            LOGGER.warn("Missing gene record for gene {}", geneId);
            return Optional.empty();
        }

        if (transcripts.isEmpty())
            // older RefSeq releases for hg19 do not have `transcript` records
            transcripts.addAll(InferTranscriptRecord.inferTranscriptRecords(gene, exons));

        return Optional.of(new GtfGeneData(gene, transcripts, exons, startCodons, stopCodons));
    }

    private static <T> Optional<T> reportMissingAttributesAndReturn(GtfRecord record,
                                                                    Set<String> mandatoryGeneAttributeNames) {
        List<String> missingAttributes = new ArrayList<>(mandatoryGeneAttributeNames.size());
        for (String attribute : mandatoryGeneAttributeNames) {
            if (!record.hasAttribute(attribute)) {
                missingAttributes.add(attribute);
            }
        }
        String missing = missingAttributes.stream().collect(Collectors.joining("`, `", "`", "`"));
        LOGGER.warn("Missing required attributes {} in record `{}`", missing, record);
        return Optional.empty();
    }

    protected static BufferedReader openForReading(Path path) throws IOException {
        LOGGER.debug("Opening Gencode GTF file at `{}`", path.toAbsolutePath());
        if (path.toFile().getName().endsWith(".gz")) {
            LOGGER.debug("Assuming the file is GZipped");
            return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path.toFile()))));
        } else {
            LOGGER.debug("Opening GTF as a plain text");
            return Files.newBufferedReader(path);
        }
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = !queue.isEmpty();
        if (!hasNext) {
            // The queue is empty if there are no genes left. We can close the reader now
            if (reader != null) {
                LOGGER.debug("Closing the file.");
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.warn("Error closing the reader: {}", e.getMessage(), e);
                }
            }
        }
        return hasNext;
    }

    @Override
    public GENE next() {
        GENE current = queue.poll();
        if (queue.isEmpty())
            readNextContig();

        return current;
    }

    protected static List<Coordinates> processExons(List<GtfRecord> exonRecords) {
        Coordinates[] exons = new Coordinates[exonRecords.size()];
        for (GtfRecord exon : exonRecords) {
            String en = exon.firstAttribute(EXON_NUMBER_ATTRIBUTE_KEY);
            int exonNumber = Integer.parseInt(en);
            // the first exon has idx 1, and arrays start from 0
            exons[exonNumber - 1] = Coordinates.of(exon.coordinateSystem(), exon.start(), exon.end());
        }

        return Arrays.asList(exons);
    }

    protected static Optional<Biotype> parseBiotype(String biotype) {
        switch (biotype.toLowerCase()) {
            case "protein_coding":
            case "rnase_mrp_rna":
                // See https://hpo.jax.org/app/browse/gene/6023
                return Optional.of(Biotype.protein_coding);
            case "primary_transcript":
                return Optional.of(Biotype.primary_transcript);
            case "mrna":
                return Optional.of(Biotype.mRNA);
            // --- ncRNA ---
            case "ncrna":
                return Optional.of(Biotype.ncRNA);
            case "mirna":
                return Optional.of(Biotype.miRNA);
            case "rrna":
                return Optional.of(Biotype.rRNA);
            case "srna":
                return Optional.of(Biotype.sRNA);
            case "trna":
                return Optional.of(Biotype.tRNA);
            case "scrna":
                return Optional.of(Biotype.scRNA);
            case "snrna":
                return Optional.of(Biotype.snRNA);
            case "asrna":
            case "antisense_rna":
                return Optional.of(Biotype.asRNA);
            case "scarna":
                return Optional.of(Biotype.scaRNA);
            case "snorna":
                return Optional.of(Biotype.snoRNA);
            case "y_rna":
                return Optional.of(Biotype.yRNA);
            case "vault_rna":
                return Optional.of(Biotype.vaultRNA);
            case "misc_rna":
                return Optional.of(Biotype.misc_RNA);


            // --- long non-coding RNA
            case "lncrna":
            case "lnc_rna":
                return Optional.of(Biotype.lncRNA);
            case "telomerase_rna":
                return Optional.of(Biotype.trRNA);

            case "pseudogene":
            case "ncrna_pseudogene":
                return Optional.of(Biotype.pseudogene);

            case "processed_pseudogene":
                return Optional.of(Biotype.processed_pseudogene);
            case "transcribed_pseudogene":
                return Optional.of(Biotype.transcribed_pseudogene);
            case "transcribed_processed_pseudogene":
                return Optional.of(Biotype.transcribed_processed_pseudogene);
            case "translated_processed_pseudogene":
                return Optional.of(Biotype.translated_processed_pseudogene);
            case "transcribed_unprocessed_pseudogene":
                return Optional.of(Biotype.transcribed_unprocessed_pseudogene);
            case "translated_unprocessed_pseudogene":
                return Optional.of(Biotype.translated_unprocessed_pseudogene);
            case "unitary_pseudogene":
                return Optional.of(Biotype.unitary_pseudogene);
            case "transcribed_unitary_pseudogene":
                return Optional.of(Biotype.transcribed_unitary_pseudogene);
            case "unprocessed_pseudogene":
                return Optional.of(Biotype.unprocessed_pseudogene);
            case "polymorphic_pseudogene":
                return Optional.of(Biotype.polymorphic_pseudogene);

            case "ig_c_gene":
            case "c_gene_segment":
            case "c_region":
                return Optional.of(Biotype.IG_C_gene);
            case "ig_j_gene":
            case "j_segment":
            case "j_gene_segment":
                return Optional.of(Biotype.IG_J_gene);
            case "ig_v_gene":
            case "v_segment":
            case "v_gene_segment":
                return Optional.of(Biotype.IG_V_gene);
            case "ig_d_gene":
            case "d_segment":
            case "d_gene_segment":
                return Optional.of(Biotype.IG_D_gene);

            case "ig_pseudogene":
                return Optional.of(Biotype.IG_pseudogene);
            case "ig_c_pseudogene":
            case "c_region_pseudogene":
                return Optional.of(Biotype.IG_C_pseudogene);
            case "ig_j_pseudogene":
            case "j_segment_pseudogene":
                return Optional.of(Biotype.IG_J_pseudogene);
            case "ig_v_pseudogene":
            case "v_segment_pseudogene":
                return Optional.of(Biotype.IG_V_pseudogene);

            case "tr_c_gene":
                return Optional.of(Biotype.TR_C_gene);
            case "tr_j_gene":
                return Optional.of(Biotype.TR_J_gene);
            case "tr_v_gene":
                return Optional.of(Biotype.TR_V_gene);
            case "tr_d_gene":
                return Optional.of(Biotype.TR_D_gene);
            case "tr_v_pseudogene":
                return Optional.of(Biotype.TR_V_pseudogene);
            case "tr_j_pseudogene":
                return Optional.of(Biotype.TR_J_pseudogene);

            case "mt_rrna":
                return Optional.of(Biotype.MT_rRNA);
            case "mt_trna":
                return Optional.of(Biotype.MT_tRNA);
            case "transcript":
                return Optional.of(Biotype.transcript);
            case "ribozyme":
            case "rnase_p_rna":
                return Optional.of(Biotype.ribozyme);
            case "tec":
            case "other":
            case "rrna_pseudogene":
            default:
                return Optional.of(Biotype.unknown);
        }
    }

    protected static Optional<Coordinates> createCdsCoordinates(GtfRecord startCodon,
                                                                GtfRecord stopCodon,
                                                                String txId,
                                                                GtfRecord tx) {
        if (startCodon == null && stopCodon == null) {
            // should be non-coding transcript
            return Optional.empty();
        } else {
            // should be coding transcript
            if (startCodon == null || stopCodon == null) {
                if (startCodon == null) {
                    LOGGER.warn("Start codon is missing for transcript `{}`: {}", txId, tx);
                } else {
                    LOGGER.warn("Stop codon is missing for transcript `{}`: {}", txId, tx);
                }
                return Optional.empty();
            } else {
                // it does not really matter what coordinate system we use as long as we get the coordinates right
                CoordinateSystem cs = tx.location().coordinateSystem();
                Coordinates cds = Coordinates.of(cs,
                        startCodon.startWithCoordinateSystem(cs),
                        stopCodon.endWithCoordinateSystem(cs));
                return Optional.of(cds);
            }
        }
    }
}
