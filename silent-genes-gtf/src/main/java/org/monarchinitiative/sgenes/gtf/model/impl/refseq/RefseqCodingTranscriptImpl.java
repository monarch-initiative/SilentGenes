package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.RefseqMetadata;
import org.monarchinitiative.sgenes.model.CodingTranscript;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Objects;

public class RefseqCodingTranscriptImpl extends RefseqNoncodingTranscript implements CodingTranscript {

    private final Coordinates cdsCoordinates;

    public static RefseqCodingTranscriptImpl of(TranscriptIdentifier id,
                                                GenomicRegion location,
                                                List<Coordinates> exons,
                                                RefseqMetadata refseqMetadata,
                                                Coordinates cdsCoordinates) {
        return new RefseqCodingTranscriptImpl(id, location, exons, refseqMetadata, cdsCoordinates);
    }

    private RefseqCodingTranscriptImpl(TranscriptIdentifier id,
                                         GenomicRegion location,
                                         List<Coordinates> exons,
                                         RefseqMetadata refseqMetadata,
                                         Coordinates cdsCoordinates) {
        super(id, location, exons, refseqMetadata);
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
        RefseqCodingTranscriptImpl that = (RefseqCodingTranscriptImpl) o;
        return Objects.equals(cdsCoordinates, that.cdsCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cdsCoordinates);
    }

    @Override
    public String toString() {
        return "RefseqCodingTranscriptImpl{" +
                "id=" + id() +
                ", location=" + location() +
                ", exons=" + exons() +
                ", refseqMetadata=" + refseqMetadata() +
                ", cdsCoordinates=" + cdsCoordinates +
                "}";
    }
}
