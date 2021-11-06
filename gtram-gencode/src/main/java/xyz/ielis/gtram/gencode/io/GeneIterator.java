package xyz.ielis.gtram.gencode.io;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ielis.gtram.gencode.model.GencodeGene;
import xyz.ielis.gtram.gencode.model.GencodeTranscript;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class GeneIterator implements Iterator<GencodeGene>, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneIterator.class);

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
            LOGGER.warn("Error opening GTF at `{}`", gencodeGtfPath);
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
        // TODO: 11/5/21 continue
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
                    // the gene record must have `hgnc_id` and `gene_name` attributes
                    if (!record.hasAttribute("hgnc_id")) {
                        LOGGER.warn("Skipping gene {} that lacks HGNC ID: `{}`", geneId, record);
                        return Optional.empty();
                    }
                    if (!record.hasAttribute("gene_name")) {
                        LOGGER.warn("Skipping gene {} that lacks gene name: `{}`", geneId, record);
                        return Optional.empty();
                    }
                    gene = record;
                    break;
                case TRANSCRIPT:
                    String txTxId = record.attribute("transcript_id");
                    if (txTxId == null) {
                        LOGGER.warn("Missing `transcript_id` in transcript record for {}: `{}`", geneId, record);
                        break;
                    }
                    transcripts.add(record);
                    break;
                case EXON:
                    String exonTxId = record.attribute("transcript_id");
                    if (exonTxId == null) {
                        LOGGER.warn("Missing `transcript_id` in exon record for {}: `{}`", geneId, record);
                        break;
                    }
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

//        GeneDefault.Builder builder = GeneDefault.builder()
//                // the attributes are present (checked above)
//                .geneSymbol(gene.attribute("gene_name"))
//                .accessionId(TermId.of(gene.attribute("hgnc_id")));
//
//        List<Transcript> txs = new ArrayList<>(transcripts.size());
//        for (GtfRecord txRecord : transcripts) {
//            String txId = txRecord.attribute("transcript_id"); // the attribute is present (checked above)
//            List<GtfRecord> txExons = exons.get(txId);
//            GtfRecord startCodon = startCodons.get(txId);
//            GtfRecord stopCodon = stopCodons.get(txId);
//            Optional<Transcript> transcript = processTranscript(txId, txRecord, txExons, startCodon, stopCodon);
//            transcript.ifPresent(txs::add);
//        }
//        if (txs.isEmpty()) {
//            LOGGER.warn("No transcripts could be parsed for gene `{}`", geneId);
//            return Optional.empty();
//        }

//        return Optional.of(builder.addAllTranscripts(txs).build());
        return Optional.empty();
    }

    private static Optional<GencodeTranscript> processTranscript(String txId,
                                                                 GtfRecord tx,
                                                                 List<GtfRecord> exons,
                                                                 GtfRecord startCodon,
                                                                 GtfRecord stopCodon) {
        if (startCodon == null && stopCodon == null) {
            // should be non-coding transcript
            return processNoncodingTranscript(txId, tx, exons);
        } else {
            // should be coding transcript
            if (startCodon == null || stopCodon == null) {
                LOGGER.warn("Start codon or stop codon are missing for transcript `{}`", txId);
                return Optional.empty();
            }

            return processCodingTranscript(txId, tx, exons, startCodon.coordinates(), stopCodon.coordinates());
        }
    }

    private static Optional<GencodeTranscript> processNoncodingTranscript(String txId,
                                                                   GtfRecord tx,
                                                                   List<GtfRecord> exonRecords) {
        List<Coordinates> exons = processExons(exonRecords);
//        Transcript transcript = Transcript.noncoding(tx.contig(), tx.strand(), tx.coordinateSystem(), tx.start(), tx.end(), txId, exons);
//        return Optional.of(transcript);
        return Optional.empty();
    }

    private static Optional<GencodeTranscript> processCodingTranscript(String txId,
                                                                GtfRecord tx,
                                                                List<GtfRecord> exonRecords,
                                                                Coordinates startCodon,
                                                                Coordinates stopCodon) {
        List<Coordinates> exons = processExons(exonRecords);
        int cdsStart = startCodon.startWithCoordinateSystem(tx.coordinateSystem());
        int cdsEnd = stopCodon.endWithCoordinateSystem(tx.coordinateSystem()) - stopCodon.length(); // stop codon is NOT part of the CDS!
//        CodingTranscript transcript = CodingTranscript.of(tx.contig(), tx.strand(), tx.coordinateSystem(), tx.start(), tx.end(), txId, exons, cdsStart, cdsEnd);
//        return Optional.of(transcript);
        return Optional.empty();
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
     * Read GTF content and
     */
    private void readNextContig() {
        List<GtfRecord> contigRecords = readContigRecords();
        processRecordsToGenes(contigRecords);
    }

    private void processRecordsToGenes(List<GtfRecord> contigRecords) {
        Map<String, List<GtfRecord>> recordByGeneId = contigRecords.stream()
                .collect(Collectors.groupingBy(GtfRecord::geneId));

        List<GencodeGene> genes = recordByGeneId.entrySet().stream()
                .map(e -> assembleGene(e.getKey(), e.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableList());

        queue.addAll(genes);
    }

    private List<GtfRecord> readContigRecords() {
        List<GtfRecord> contigRecords = new ArrayList<>();
        try {
            if (firstRecordOfNextContig != null)
                // the first record is null before we're processing the first contig
                contigRecords.add(firstRecordOfNextContig);

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
