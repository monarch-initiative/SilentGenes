package xyz.ielis.silent.genes.gencode.io;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ielis.silent.genes.gencode.impl.GencodeCodingTranscript;
import xyz.ielis.silent.genes.gencode.impl.GencodeGeneImpl;
import xyz.ielis.silent.genes.gencode.impl.GencodeNoncodingTranscript;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.GeneIdentifier;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class GeneIterator implements Iterator<GencodeGene> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneIterator.class);

    // These are required attributes for gene and transcript record. Any record lacking one or more attribute is skipped
    // and the warning is logged.
    // Note that `gene_id` is mandatory argument and the GTF lines lacking the `gene_id` do
    // not make it into `GtfRecord`.
    private static final Set<String> MANDATORY_GENE_ATTRIBUTE_NAMES = Set.of("gene_type", "gene_name", "level");
    private static final Set<String> MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES = Set.of("transcript_id", "transcript_type", "transcript_name", "level");
    private static final Set<String> MANDATORY_EXON_ATTRIBUTE_NAMES = Set.of("transcript_id", "exon_id", "exon_number");
    private static final String NCBI_GENE_ID_IS_NA = null; // Gencode does not provide NCBIGene IDs

    private final GenomicAssembly genomicAssembly;
    private final Queue<GencodeGene> queue = new LinkedList<>();
    private BufferedReader reader;
    private String currentContig;
    private GtfRecord firstRecordOfNextContig;

    GeneIterator(Path gencodeGtfPath, GenomicAssembly genomicAssembly) {
        this.genomicAssembly = genomicAssembly;
        try {
            reader = openForReading(gencodeGtfPath);
            readNextContig();
        } catch (IOException e) {
            LOGGER.warn("Error opening GTF at `{}`: {}", gencodeGtfPath, e.getMessage());
        }
    }

    private static BufferedReader openForReading(Path path) throws IOException {
        LOGGER.debug("Opening Gencode GTF file at `{}`", path.toAbsolutePath());
        if (path.toFile().getName().endsWith(".gz")) {
            LOGGER.debug("Assuming the file is GZipped");
            return new BufferedReader(new InputStreamReader(new GzipCompressorInputStream(new FileInputStream(path.toFile()))));
        } else {
            LOGGER.debug("Opening GTF as a plain text");
            return Files.newBufferedReader(path);
        }
    }

    private static Optional<GencodeGene> assembleGene(String geneId, List<GtfRecord> records) {
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
                    if (!record.attributes().containsAll(MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES)) {
                        return reportMissingAttributesAndReturn(record, MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES);
                    }
                    transcripts.add(record);
                    break;
                case EXON:
                    if (!record.attributes().containsAll(MANDATORY_EXON_ATTRIBUTE_NAMES)) {
                        return reportMissingAttributesAndReturn(record, MANDATORY_EXON_ATTRIBUTE_NAMES);
                    }
                    String exonTxId = record.attribute("transcript_id");
                    exons.putIfAbsent(exonTxId, new ArrayList<>());
                    exons.get(exonTxId).add(record);
                    break;
                case START_CODON:
                    String startCodonTxId = record.attribute("transcript_id");
                    if (startCodonTxId == null) {
                        LOGGER.warn("Missing `transcript_id` in start codon record for {}: `{}`", geneId, record);
                        break;
                    }
                    startCodons.put(startCodonTxId, record);
                    break;
                case STOP_CODON:
                    String stopCodonTxId = record.attribute("transcript_id");
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
            LOGGER.warn("Did not find gene GTF row for gene {}", geneId);
            return Optional.empty();
        }

        // id, type, status, name, tags
        if (!gene.attributes().containsAll(MANDATORY_GENE_ATTRIBUTE_NAMES)) {
            return reportMissingAttributesAndReturn(gene, MANDATORY_GENE_ATTRIBUTE_NAMES);
        }


        String hgncId = gene.attribute("hgnc_id");
        GeneIdentifier geneIdentifier = GeneIdentifier.of(geneId, gene.attribute("gene_name"), hgncId, NCBI_GENE_ID_IS_NA);

        Optional<Biotype> biotype = parseBiotype(gene.attribute("gene_type"));
        if (biotype.isEmpty()) {
            LOGGER.warn("Unable to parse biotype level `{}` for gene `{}`", gene.attribute("gene_name"), geneId);
            return Optional.empty();
        }

        Optional<EvidenceLevel> evidenceLevel = parseEvidenceLevel(gene.attribute("level"));
        if (evidenceLevel.isEmpty()) {
            LOGGER.warn("Unable to parse evidence level `{}` for gene `{}`", gene.attribute("level"), geneId);
            return Optional.empty();
        }

        // transcripts
        List<GencodeTranscript> txs = new ArrayList<>(transcripts.size());
        for (GtfRecord txRecord : transcripts) {
            String txId = txRecord.attribute("transcript_id"); // the attribute is present (checked above)
            List<GtfRecord> txExons = exons.get(txId);
            GtfRecord startCodon = startCodons.get(txId);
            GtfRecord stopCodon = stopCodons.get(txId);
            Optional<GencodeTranscript> transcript = processTranscript(txId, txRecord, txExons, startCodon, stopCodon);
            transcript.ifPresent(txs::add);
        }
        if (txs.isEmpty()) {
            LOGGER.warn("No transcripts could be parsed for gene `{}`", geneId);
            return Optional.empty();
        }

        return Optional.of(GencodeGeneImpl.of(gene.location(), geneIdentifier, biotype.get(), evidenceLevel.get(), txs, gene.tags()));
    }

    private static Optional<GencodeTranscript> processTranscript(String txId,
                                                                 GtfRecord tx,
                                                                 List<GtfRecord> exonRecords,
                                                                 GtfRecord startCodon,
                                                                 GtfRecord stopCodon) {
        Optional<EvidenceLevel> level = parseEvidenceLevel(tx.attribute("level"));
        if (level.isEmpty()) {
            LOGGER.warn("Unable to parse evidence level `{}` for gene `{}`", tx.attribute("level"), txId);
            return Optional.empty();
        }

        Optional<Biotype> biotype = parseBiotype(tx.attribute("transcript_type"));
        if (biotype.isEmpty()) {
            LOGGER.warn("Unable to parse biotype level `{}` for gene `{}`", tx.attribute("gene_name"), txId);
            return Optional.empty();
        }
        TranscriptIdentifier txIdentifier = TranscriptIdentifier.of(txId, tx.attribute("transcript_name"), tx.attribute("ccdsid"));
        List<Coordinates> exons = processExons(exonRecords);

        if (startCodon == null && stopCodon == null) {
            // should be non-coding transcript
            return Optional.of(GencodeNoncodingTranscript.of(tx.location(), txIdentifier, biotype.get(), level.get(), exons, tx.tags()));
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
                CoordinateSystem cs = tx.location().coordinateSystem();
                Coordinates cds = Coordinates.of(cs,
                        startCodon.startWithCoordinateSystem(cs),
                        stopCodon.endWithCoordinateSystem(cs));
                return Optional.of(GencodeCodingTranscript.of(tx.location(), txIdentifier, cds, biotype.get(), level.get(), exons, tx.tags()));
            }
        }
    }

    private static Optional<Biotype> parseBiotype(String geneType) {
        switch (geneType.toLowerCase()) {
            case "protein_coding":
                return Optional.of(Biotype.protein_coding);

            // --- ncRNA ---
            case "mirna":
                return Optional.of(Biotype.miRNA);
            case "rrna":
                return Optional.of(Biotype.rRNA);
            case "srna":
                return Optional.of(Biotype.sRNA);
            case "scrna":
                return Optional.of(Biotype.scRNA);
            case "snrna":
                return Optional.of(Biotype.snRNA);
            case "scarna":
                return Optional.of(Biotype.scaRNA);
            case "snorna":
                return Optional.of(Biotype.snoRNA);
            case "vault_rna":
                return Optional.of(Biotype.vaultRNA);
            case "misc_rna":
                return Optional.of(Biotype.misc_RNA);


            // --- long non-coding RNA
            case "lncrna":
                return Optional.of(Biotype.lncRNA);

            case "pseudogene":
                return Optional.of(Biotype.pseudogene);

            case "processed_pseudogene":
                return Optional.of(Biotype.processed_pseudogene);
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
                return Optional.of(Biotype.IG_C_gene);
            case "ig_j_gene":
                return Optional.of(Biotype.IG_J_gene);
            case "ig_v_gene":
                return Optional.of(Biotype.IG_V_gene);
            case "ig_d_gene":
                return Optional.of(Biotype.IG_D_gene);

            case "ig_pseudogene":
                return Optional.of(Biotype.IG_pseudogene);
            case "ig_c_pseudogene":
                return Optional.of(Biotype.IG_C_pseudogene);
            case "ig_j_pseudogene":
                return Optional.of(Biotype.IG_J_pseudogene);
            case "ig_v_pseudogene":
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
            case "ribozyme":
            case "tec":
            case "rrna_pseudogene":
            default:
                return Optional.of(Biotype.unknown);
        }

    }

    private static Optional<EvidenceLevel> parseEvidenceLevel(String level) {
        switch (level) {
            case "1":
                return Optional.of(EvidenceLevel.VERIFIED);
            case "2":
                return Optional.of(EvidenceLevel.MANUALLY_ANNOTATED);
            case "3":
                return Optional.of(EvidenceLevel.AUTOMATICALLY_ANNOTATED);
            default:
                return Optional.empty();
        }
    }

    private static List<Coordinates> processExons(List<GtfRecord> exonRecords) {
        Coordinates[] exons = new Coordinates[exonRecords.size()];
        for (GtfRecord exon : exonRecords) {
            String en = exon.attribute("exon_number");
            int exonNumber = Integer.parseInt(en);
            // the first exon has idx 1, and arrays start from 0
            exons[exonNumber - 1] = Coordinates.of(exon.coordinateSystem(), exon.start(), exon.end());
        }

        return Arrays.asList(exons);
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
    public GencodeGene next() {
        GencodeGene current = queue.poll();
        if (queue.isEmpty())
            readNextContig();
        return current;
    }

    /**
     * Transform the lines of records located on the next contig into {@link GtfRecord}s, and convert the records
     * to {@link GencodeGene}s.
     */
    private void readNextContig() {
        List<GtfRecord> contigRecords = readContigRecords();
        List<GencodeGene> genes = processRecordsToGenes(contigRecords);
        queue.addAll(genes);
    }

    private List<GencodeGene> processRecordsToGenes(List<GtfRecord> contigRecords) {
        Map<String, List<GtfRecord>> recordByGeneId = contigRecords.stream()
                .collect(Collectors.groupingBy(GtfRecord::geneId, Collectors.toUnmodifiableList()));

        return recordByGeneId.entrySet().stream()
                .map(e -> assembleGene(e.getKey(), e.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<GtfRecord> readContigRecords() {
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

                    if (currentContig.equals(record.contigName())) {
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
}
