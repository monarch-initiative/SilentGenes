package org.monarchinitiative.sgenes.model.impl;

import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.sgenes.model.TranscriptMetadata;
import org.monarchinitiative.sgenes.model.base.BaseCodingTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;

public class CodingTranscriptDefault extends BaseCodingTranscript {

    public static CodingTranscriptDefault of(TranscriptIdentifier id,
                                             GenomicRegion location,
                                             List<Coordinates> exons,
                                             Coordinates cdsCoordinates,
                                             TranscriptMetadata metadata) {
        return new CodingTranscriptDefault(id, location, exons, cdsCoordinates, metadata);
    }

    private CodingTranscriptDefault(TranscriptIdentifier id,
                                    GenomicRegion location,
                                    List<Coordinates> exons,
                                    Coordinates cdsCoordinates,
                                    TranscriptMetadata metadata) {
        super(id, location, exons, cdsCoordinates, metadata);
    }
    @Override
    public String toString() {
        return "CodingTranscriptDefault{" +
                "id=" + id() +
                ", location=" + location() +
                ", exons=" + exons() +
                ", cdsCoordinates=" + cdsCoordinates() +
                '}';
    }
}
