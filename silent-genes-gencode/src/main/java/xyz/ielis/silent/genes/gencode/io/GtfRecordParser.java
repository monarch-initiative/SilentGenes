package xyz.ielis.silent.genes.gencode.io;

import org.monarchinitiative.svart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GtfRecordParser {

    static final CoordinateSystem COORDINATE_SYSTEM = CoordinateSystem.oneBased(); // GTF invariant
    private static final Logger LOGGER = LoggerFactory.getLogger(GtfRecordParser.class);
    private static final Pattern GENE_ID_PATTERN = Pattern.compile("gene_id\\s*\"(?<value>ENSG\\d{11}\\.\\d+(_PAR_Y)?)\"");
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("(?<key>[\\w_]+)\\s*(?<value>[\\w\\d_:./\\-\"]+)");
    // max number of fields in the GTF file used to develop this parser. A good default capacity for the attribute map
    private static final int N_ATTRIBUTE_FIELDS = 26;

    private GtfRecordParser() {
    }


    static Optional<GtfRecord> parseLine(String line, GenomicAssembly assembly) {
        String[] token = line.split("\t");
        // contig
        Contig contig = assembly.contigByName(token[0]);
        if (contig.isUnknown()) {
            LOGGER.warn("Skipping a line with unknown contig `{}`: `{}`", token[0], line);
            return Optional.empty();
        }

        // strand & coordinate system
        Optional<Strand> strand = parseStrand(token[6]);
        if (strand.isEmpty()) return Optional.empty();
        Optional<Coordinates> coordinates = parseCoordinates(contig, strand.get(), token[3], token[4]);
        if (coordinates.isEmpty()) return Optional.empty();

        // source, feature, frame
        Optional<GtfSource> source = parseSource(token[1]);
        if (source.isEmpty()) return Optional.empty();

        Optional<GtfFeature> feature = parseFeature(token[2]);
        if (feature.isEmpty()) return Optional.empty();

        Optional<GtfFrame> frame = parseFrame(token[7]);
        if (frame.isEmpty()) return Optional.empty();

        Optional<String> geneId = parseGeneId(token[8]);
        if (geneId.isEmpty()) return Optional.empty();

        Map<String, String> attributes = parseAttributes(token[8]);
        return Optional.of(GtfRecord.of(contig, strand.get(), coordinates.get(), source.get(), feature.get(), frame.get(), geneId.get(), attributes));
    }

    private static Optional<GtfFrame> parseFrame(String payload) {
        switch (payload) {
            case ".":
                return Optional.of(GtfFrame.NA);
            case "0":
                return Optional.of(GtfFrame.ZERO);
            case "1":
                return Optional.of(GtfFrame.ONE);
            case "2":
                return Optional.of(GtfFrame.TWO);
            default:
                LOGGER.warn("Unknown GTF frame: `{}`", payload);
                return Optional.empty();
        }
    }

    private static Optional<Strand> parseStrand(String strand) {
        switch (strand) {
            case "+":
                return Optional.of(Strand.POSITIVE);
            case "-":
                return Optional.of(Strand.NEGATIVE);
            default:
                LOGGER.warn("Unknown strand " + strand);
                return Optional.empty();
        }
    }

    private static Optional<Coordinates> parseCoordinates(Contig contig, Strand strand, String startPos, String endPos) {
        // GTF provides coordinates on POSITIVE strand even if the actual strand of the feature is negative.
        // We must adjust the coordinates to strand and contig.
        try {
            int start = Integer.parseInt(startPos);
            int end = Integer.parseInt(endPos);
            if (strand.isPositive()) {
                return Optional.of(Coordinates.of(COORDINATE_SYSTEM, start, end));
            } else {
                int startOnNegative = Coordinates.invertPosition(COORDINATE_SYSTEM, contig, end);
                int endOnNegative = Coordinates.invertPosition(COORDINATE_SYSTEM, contig, start);
                return Optional.of(Coordinates.of(COORDINATE_SYSTEM, startOnNegative, endOnNegative));
            }
        } catch (NumberFormatException e) {
            LOGGER.warn("Error parsing GTF coordinates. Start: `" + startPos + "`, end: `" + endPos + "`");
            return Optional.empty();
        }
    }

    private static Optional<GtfSource> parseSource(String payload) {
        switch (payload) {
            case "ENSEMBL":
                return Optional.of(GtfSource.ENSEMBL);
            case "HAVANA":
                return Optional.of(GtfSource.HAVANA);
            default:
                LOGGER.warn("Unknown GTF record source: `{}`", payload);
                return Optional.empty();
        }
    }

    private static Optional<GtfFeature> parseFeature(String payload) {
        switch (payload) {
            case "exon":
                return Optional.of(GtfFeature.EXON);
            case "CDS":
                return Optional.of(GtfFeature.CDS);
            case "UTR":
                return Optional.of(GtfFeature.UTR);
            case "transcript":
                return Optional.of(GtfFeature.TRANSCRIPT);
            case "start_codon":
                return Optional.of(GtfFeature.START_CODON);
            case "stop_codon":
                return Optional.of(GtfFeature.STOP_CODON);
            case "gene":
                return Optional.of(GtfFeature.GENE);
            case "Selenocysteine":
                return Optional.of(GtfFeature.SELENOCYSTEINE);
            default:
                LOGGER.warn("Unknown GTF feature type: `{}`", payload);
                return Optional.empty();
        }
    }

    private static Optional<String> parseGeneId(String payload) {
        Matcher matcher = GENE_ID_PATTERN.matcher(payload);
        if (matcher.find()) {
            return Optional.ofNullable(matcher.group("value"));
        }
        return Optional.empty();
    }

    private static Map<String, String> parseAttributes(String payload) {
        Map<String, String> attributeMap = new HashMap<>(N_ATTRIBUTE_FIELDS);
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(payload);
        while (matcher.find()) {
            String key = matcher.group("key");
            if ("gene_id".equals(key)) continue; // we store the gene_id elsewhere
            String value = matcher.group("value");
            if (value.startsWith("\"") && value.endsWith("\"")) {
                try {
                    attributeMap.put(key, value.substring(1, value.length() - 1));
                } catch (StringIndexOutOfBoundsException e) {
                    System.err.println("Whoops: " + payload);
                    throw e;
                }
            } else {
                attributeMap.put(key, value);
            }
        }
        return attributeMap;
    }

}
