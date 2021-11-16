package xyz.ielis.silent.genes.gencode.io;

import org.monarchinitiative.svart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GtfRecordParser {

    static final CoordinateSystem COORDINATE_SYSTEM = CoordinateSystem.oneBased(); // GTF invariant
    private static final Logger LOGGER = LoggerFactory.getLogger(GtfRecordParser.class);
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("(?<key>[\\w_]+)\\s*(?<value>[\\w\\d_:./\\-\"]+)");
    private static final Pattern TAG_PATTERN = Pattern.compile("tag\\s*\"(?<value>[\\w\\d_:./\\-]+)\"");
    // max number of fields in the GTF file used to develop this parser. A good default capacity for the attribute map
    private static final int N_ATTRIBUTE_FIELDS = 26;

    private GtfRecordParser() {
    }


    static Optional<GtfRecord> parseLine(String line, GenomicAssembly assembly) {
        String[] token = line.split("\t");
        try {
            GenomicRegion location = parseLocation(assembly, token[0], token[6], token[3], token[4]);
            GtfSource source = parseSource(token[1]);
            GtfFeature feature = parseFeature(token[2]);
            GtfFrame frame = parseFrame(token[7]);
            Map<String, String> attributes = parseAttributes(token[8]);
            Set<String> tags = parseTags(token[8]);

            return Optional.of(GtfRecord.of(location, source, feature, frame, attributes, tags));
        } catch (GtfParseException e) {
            LOGGER.warn("{}. Line {}", e.getMessage(), line);
            return Optional.empty();
        }
    }

    private static GenomicRegion parseLocation(GenomicAssembly assembly,
                                               String contigName,
                                               String strandValue,
                                               String start,
                                               String end) throws GtfParseException {
        // Parse contig, strand & coordinate system
        Contig contig = assembly.contigByName(contigName);
        if (contig.isUnknown())
            throw new GtfParseException("Unknown contig `" + contigName + '`');

        Strand strand = parseStrand(strandValue);
        Coordinates coordinates = parseCoordinates(contig, strand, start, end);

        return GenomicRegion.of(contig, strand, coordinates);
    }

    private static GtfFrame parseFrame(String payload) throws GtfParseException {
        switch (payload) {
            case ".":
                return GtfFrame.NA;
            case "0":
                return GtfFrame.ZERO;
            case "1":
                return GtfFrame.ONE;
            case "2":
                return GtfFrame.TWO;
            default:
                throw new GtfParseException(String.format("Unknown GTF frame: `%s`", payload));
        }
    }

    private static Strand parseStrand(String strand) throws GtfParseException {
        switch (strand) {
            case "+":
                return Strand.POSITIVE;
            case "-":
                return Strand.NEGATIVE;
            default:
                throw new GtfParseException("Unknown strand `" + strand + '`');
        }
    }

    private static Coordinates parseCoordinates(Contig contig, Strand strand, String startPos, String endPos) throws GtfParseException {
        // GTF provides coordinates on POSITIVE strand even if the actual strand of the feature is negative.
        // We must adjust the coordinates to strand and contig.
        try {
            int start = Integer.parseInt(startPos);
            int end = Integer.parseInt(endPos);
            if (strand.isPositive()) {
                return Coordinates.of(COORDINATE_SYSTEM, start, end);
            } else {
                int startOnNegative = Coordinates.invertPosition(COORDINATE_SYSTEM, contig, end);
                int endOnNegative = Coordinates.invertPosition(COORDINATE_SYSTEM, contig, start);
                return Coordinates.of(COORDINATE_SYSTEM, startOnNegative, endOnNegative);
            }
        } catch (NumberFormatException e) {
            throw new GtfParseException("Unparsable coordinates: start=" + startPos + ", end=" + endPos);
        }
    }

    private static GtfSource parseSource(String payload) throws GtfParseException {
        switch (payload) {
            case "ENSEMBL":
                return GtfSource.ENSEMBL;
            case "HAVANA":
                return GtfSource.HAVANA;
            default:
                throw new GtfParseException(String.format("Unknown GTF record source: `%s`", payload));
        }
    }

    private static GtfFeature parseFeature(String payload) throws GtfParseException {
        switch (payload) {
            case "exon":
                return GtfFeature.EXON;
            case "CDS":
                return GtfFeature.CDS;
            case "UTR":
                return GtfFeature.UTR;
            case "transcript":
                return GtfFeature.TRANSCRIPT;
            case "start_codon":
                return GtfFeature.START_CODON;
            case "stop_codon":
                return GtfFeature.STOP_CODON;
            case "gene":
                return GtfFeature.GENE;
            case "Selenocysteine":
                return GtfFeature.SELENOCYSTEINE;
            default:
                throw new GtfParseException(String.format("Unknown GTF feature type: `%s`", payload));
        }
    }

    private static Map<String, String> parseAttributes(String payload) throws GtfParseException {
        Map<String, String> attributes = new HashMap<>(N_ATTRIBUTE_FIELDS);
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(payload);
        while (matcher.find()) {
            String key = matcher.group("key");
            String value = matcher.group("value");
            if (value.startsWith("\"") && value.endsWith("\"")) {
                // some attribute values are enclosed in double quotes
                String strippedQuotes = value.substring(1, value.length() - 1);
                attributes.put(key, strippedQuotes);
            } else {
                // others are not
                attributes.put(key, value);
            }
        }

        if (!attributes.containsKey("gene_id")) {
            throw new GtfParseException("The mandatory `gene_id` attribute is missing");
        }

        return attributes;
    }

    private static Set<String> parseTags(String payload) {
        Matcher matcher = TAG_PATTERN.matcher(payload);
        Set<String> builder = new HashSet<>();
        while (matcher.find()) {
            builder.add(matcher.group("value"));
        }
        return Set.copyOf(builder);
    }

}
