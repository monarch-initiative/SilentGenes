package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.GencodeMetadata;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.CodingTranscript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;
import xyz.ielis.silent.genes.model.base.BaseCodingTranscript;

import java.util.List;
import java.util.Objects;

public class GencodeCodingTranscript extends BaseCodingTranscript implements GencodeTranscript, CodingTranscript {

    private final GencodeMetadata gencodeMetadata;

    public static GencodeCodingTranscript of(TranscriptIdentifier id,
                                             GenomicRegion location,
                                             List<Coordinates> exons,
                                             Coordinates cdsCoordinates,
                                             GencodeMetadata gencodeMetadata) {
        return new GencodeCodingTranscript(id, location, exons, cdsCoordinates, gencodeMetadata);
    }

    private GencodeCodingTranscript(TranscriptIdentifier id,
                                    GenomicRegion location,
                                    List<Coordinates> exons,
                                    Coordinates cdsCoordinates,
                                    GencodeMetadata gencodeMetadata) {
        super(id, location, exons, cdsCoordinates);
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
        GencodeCodingTranscript that = (GencodeCodingTranscript) o;
        return Objects.equals(gencodeMetadata, that.gencodeMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gencodeMetadata);
    }

    @Override
    public String toString() {
        return "GencodeCodingTranscript{" +
                "gencodeMetadata=" + gencodeMetadata +
                "} " + super.toString();
    }
}
