package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.io.gtf.GtfFeature;
import org.monarchinitiative.sgenes.gtf.io.gtf.GtfFrame;
import org.monarchinitiative.sgenes.gtf.io.gtf.GtfRecord;
import org.monarchinitiative.sgenes.gtf.io.gtf.GtfSource;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class InferTranscriptRecord {

    private static final Logger LOGGER = LoggerFactory.getLogger(InferTranscriptRecord.class);

    private InferTranscriptRecord() {
    }

    static Collection<? extends GtfRecord> inferTranscriptRecords(GtfRecord gene, Map<String, List<GtfRecord>> exonMap) {
        List<GtfRecord> transcripts = new ArrayList<>(exonMap.size());
        for (Map.Entry<String, List<GtfRecord>> entry : exonMap.entrySet()) {
            String txAccession = entry.getKey();
            List<GtfRecord> exons = entry.getValue();
            if (exons.isEmpty()) {
                LOGGER.warn("No exons found for transcript {}", txAccession);
                continue;
            }

            GenomicRegion location = inferTranscriptLocation(exons);
            GtfSource source = inferSource(exons);
            Map<String, List<String>> attributes = parseAttributes(gene, exons);
            if (attributes.isEmpty())
                continue;

            Set<String> tags = Set.of(); // not used at the moment
            transcripts.add(GtfRecord.of(location, source, GtfFeature.TRANSCRIPT, GtfFrame.NA, attributes, tags));
        }

        return transcripts;
    }

    private static GenomicRegion inferTranscriptLocation(List<GtfRecord> exons) {
        int start = Integer.MAX_VALUE, end = Integer.MIN_VALUE;
        for (GtfRecord exon : exons) {
            start = Math.min(start, exon.startWithCoordinateSystem(CoordinateSystem.zeroBased()));
            end = Math.max(end, exon.endWithCoordinateSystem(CoordinateSystem.zeroBased()));
        }

        GtfRecord first = exons.get(0);
        Coordinates coordinates = Coordinates.of(CoordinateSystem.zeroBased(), start, end);
        return GenomicRegion.of(first.contig(), first.strand(), coordinates);
    }

    private static GtfSource inferSource(List<GtfRecord> exons) {
        return exons.get(0).source();
    }

    private static Map<String, List<String>> parseAttributes(GtfRecord gene, List<GtfRecord> exons) {
        GtfRecord first = exons.get(0);

        if (!first.hasAttribute("product")) {
            LOGGER.warn("Unable to infer transcript name for {}: `product` field is missing", first.firstAttribute("transcript_id"));
            return Map.of();
        }

        return Map.of(
                "gene_id", gene.attribute("gene_id"),
                "transcript_id", first.attribute("transcript_id"),
                "product", first.attribute("product"),
                "transcript_biotype", first.attribute("gbkey").stream().map(toBiotype()).collect(Collectors.toList())
        );
    }

    /**
     * Map `gbkey` field found in exon rows to `transcript_biotype`.
     */
    private static Function<String, String> toBiotype() {
        return attr -> {
            switch (attr.toLowerCase()) {
                case "mrna":
                    return "protein_coding";
                case "rrna":
                    return "rrna";
                case "ncrna":
                    return "ncrna";
                case "precursor_rna":
                case "c_region":
                case "misc_rna":
                    return "misc_rna";
                default:
                    return "unknown";
            }
        };
    }

}
