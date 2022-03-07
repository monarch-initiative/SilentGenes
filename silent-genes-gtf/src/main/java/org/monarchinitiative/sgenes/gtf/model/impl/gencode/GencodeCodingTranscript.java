package org.monarchinitiative.sgenes.gtf.model.impl.gencode;

import org.monarchinitiative.sgenes.gtf.model.GencodeTranscriptMetadata;
import org.monarchinitiative.sgenes.model.CodingTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;

public class GencodeCodingTranscript extends GencodeNoncodingTranscript implements CodingTranscript {

    private final Coordinates cdsCoordinates;

    public static GencodeCodingTranscript of(TranscriptIdentifier id,
                                             GenomicRegion location,
                                             List<Coordinates> exons,
                                             Coordinates cdsCoordinates,
                                             GencodeTranscriptMetadata metadata) {
        return new GencodeCodingTranscript(id, location, exons, cdsCoordinates, metadata);
    }

    private GencodeCodingTranscript(TranscriptIdentifier id,
                                    GenomicRegion location,
                                    List<Coordinates> exons,
                                    Coordinates cdsCoordinates,
                                    GencodeTranscriptMetadata metadata) {
        super(id, location, exons, metadata);
        this.cdsCoordinates = Objects.requireNonNull(cdsCoordinates, "CDS coordinates must not be null");
    }


    @Override
    public Coordinates cdsCoordinates() {
        return cdsCoordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GencodeCodingTranscript that = (GencodeCodingTranscript) o;
        return Objects.equals(cdsCoordinates, that.cdsCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cdsCoordinates);
    }

    @Override
    public String toString() {
        return "GencodeCodingTranscript{" +
                "id=" + id() +
                ", metadata=" + metadata() +
                ", location=" + location() +
                ", exons=" + exons() +
                ", cdsCoordinates=" + cdsCoordinates +
                "}";
    }
}
