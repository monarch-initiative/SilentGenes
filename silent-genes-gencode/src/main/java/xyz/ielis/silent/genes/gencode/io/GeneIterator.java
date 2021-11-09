package xyz.ielis.silent.genes.gencode.io;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ielis.silent.genes.gencode.impl.CodingTranscript;
import xyz.ielis.silent.genes.gencode.impl.GencodeGeneImpl;
import xyz.ielis.silent.genes.gencode.impl.NoncodingTranscript;
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
import java.util.zip.GZIPInputStream;

public class GeneIterator implements Iterator<GencodeGene>, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneIterator.class);

    // These are required attributes for gene and transcript record. Any record lacking one or more attribute is skipped
    // and the warning is logged.
    // Note that `gene_id` is mandatory argument and the GTF lines lacking the `gene_id` do
    // not make it into `GtfRecord`.
    private static final Set<String> MANDATORY_GENE_ATTRIBUTE_NAMES = Set.of("gene_type", "gene_name", "level");
    private static final Set<String> MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES = Set.of("transcript_id", "transcript_type", "transcript_name", "level");
    private static final Set<String> MANDATORY_EXON_ATTRIBUTE_NAMES = Set.of("transcript_id", "exon_id", "exon_number");

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
            return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path.toFile()))));
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

        // id, type, status, name
        if (!gene.attributes().containsAll(MANDATORY_GENE_ATTRIBUTE_NAMES)) {
            return reportMissingAttributesAndReturn(gene, MANDATORY_GENE_ATTRIBUTE_NAMES);
        }


        String hgncId = gene.attribute("hgnc_id");
        GeneIdentifier geneIdentifier = GeneIdentifier.of(geneId, gene.attribute("gene_name"), hgncId);
        String type = gene.attribute("gene_type");

        Optional<EvidenceLevel> evidenceLevel = parseEvidenceLevel(gene.attribute("level"));
        if (evidenceLevel.isEmpty()) {
            LOGGER.warn("Unable to parse evidence level `{}` for gene `{}`", gene.attribute("level"), geneId);
            return Optional.empty();
        }


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

        return Optional.of(GencodeGeneImpl.of(gene.location(), geneIdentifier, type, evidenceLevel.get(), txs));
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

        String type = tx.attribute("transcript_type");
        TranscriptIdentifier txIdentifier = TranscriptIdentifier.of(txId, tx.attribute("transcript_name"), tx.attribute("ccdsid"));
        List<Coordinates> exons = processExons(exonRecords);

        if (startCodon == null && stopCodon == null) {
            // should be non-coding transcript
            return Optional.of(NoncodingTranscript.of(tx.location(), txIdentifier, type, level.get(), exons));
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
                return Optional.of(CodingTranscript.of(tx.location(), txIdentifier, startCodon.coordinates(), stopCodon.coordinates(), type, level.get(), exons));
            }
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
        return !queue.isEmpty();
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

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        queue.clear();
    }
}