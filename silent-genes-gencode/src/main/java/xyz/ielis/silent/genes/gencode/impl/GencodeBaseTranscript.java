package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.GencodeMetadata;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;
import xyz.ielis.silent.genes.model.base.BaseTranscript;

import java.util.List;
import java.util.Objects;

abstract class GencodeBaseTranscript extends BaseTranscript implements GencodeTranscript {

    private final GencodeMetadata gencodeMetadata;

    GencodeBaseTranscript(TranscriptIdentifier id,
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
        GencodeBaseTranscript that = (GencodeBaseTranscript) o;
        return Objects.equals(gencodeMetadata, that.gencodeMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gencodeMetadata);
    }

    @Override
    public String toString() {
        return "GencodeBaseTranscript{" +
                "gencodeMetadata=" + gencodeMetadata +
                "} " + super.toString();
    }
}
