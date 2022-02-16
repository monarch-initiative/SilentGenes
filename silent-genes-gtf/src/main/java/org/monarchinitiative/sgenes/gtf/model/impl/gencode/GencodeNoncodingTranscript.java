package org.monarchinitiative.sgenes.gtf.model.impl.gencode;

import org.monarchinitiative.sgenes.gtf.model.GencodeMetadata;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscript;
import org.monarchinitiative.sgenes.model.base.BaseTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;

public class GencodeNoncodingTranscript extends BaseTranscript implements GencodeTranscript {

    private final GencodeMetadata gencodeMetadata;

    public static GencodeNoncodingTranscript of(TranscriptIdentifier id,
                                                GenomicRegion location,
                                                List<Coordinates> exons,
                                                GencodeMetadata gencodeMetadata) {
        return new GencodeNoncodingTranscript(id, location, exons, gencodeMetadata);
    }

    GencodeNoncodingTranscript(TranscriptIdentifier id,
                               GenomicRegion location,
                               List<Coordinates> exons,
                               GencodeMetadata gencodeMetadata) {
        super(id, location, exons);
        this.gencodeMetadata = Objects.requireNonNull(gencodeMetadata, "Gencode metadata must not be null");
    }

    @Override
    public GencodeMetadata gencodeMetadata() {
        return gencodeMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GencodeNoncodingTranscript that = (GencodeNoncodingTranscript) o;
        return Objects.equals(gencodeMetadata, that.gencodeMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gencodeMetadata);
    }

    @Override
    public String toString() {
        return "GencodeNoncodingTranscript{" +
                "gencodeMetadata=" + gencodeMetadata +
                "} " + super.toString();
    }
}
