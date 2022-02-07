package org.monarchinitiative.sgenes.model.impl;

import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.sgenes.model.base.BaseCodingTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;

public class CodingTranscriptDefault extends BaseCodingTranscript {

    public static CodingTranscriptDefault of(TranscriptIdentifier id,
                                             GenomicRegion location,
                                             List<Coordinates> exons,
                                             Coordinates cdsCoordinates) {
        return new CodingTranscriptDefault(id, location, exons, cdsCoordinates);
    }

    private CodingTranscriptDefault(TranscriptIdentifier id,
                                    GenomicRegion location,
                                    List<Coordinates> exons,
                                    Coordinates cdsCoordinates) {
        super(id, location, exons, cdsCoordinates);
    }
    @Override
    public String toString() {
        return "CodingTranscriptDefault{} " + super.toString();
    }
}
