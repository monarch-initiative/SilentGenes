package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.RefseqTranscriptMetadata;
import org.monarchinitiative.sgenes.gtf.model.RefseqSource;
import org.monarchinitiative.sgenes.model.CodingTranscript;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Objects;

public class RefseqCodingTranscript extends RefseqNoncodingTranscript implements CodingTranscript {

    private final Coordinates cdsCoordinates;

    public static RefseqCodingTranscript of(TranscriptIdentifier id,
                                            GenomicRegion location,
                                            List<Coordinates> exons,
                                            RefseqSource source,
                                            RefseqTranscriptMetadata metadata,
                                            Coordinates cdsCoordinates) {
        return new RefseqCodingTranscript(id, location, exons, metadata, source, cdsCoordinates);
    }

    private RefseqCodingTranscript(TranscriptIdentifier id,
                                   GenomicRegion location,
                                   List<Coordinates> exons,
                                   RefseqTranscriptMetadata metadata,
                                   RefseqSource source,
                                   Coordinates cdsCoordinates) {
        super(id, location, exons, source, metadata);
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
        RefseqCodingTranscript that = (RefseqCodingTranscript) o;
        return Objects.equals(cdsCoordinates, that.cdsCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cdsCoordinates);
    }

    @Override
    public String toString() {
        return "RefseqCodingTranscript" +
                "id=" + id() +
                ", location=" + location() +
                ", exons=" + exons() +
                ", metadata=" + metadata() +
                ", cdsCoordinates=" + cdsCoordinates +
                "}";
    }
}
