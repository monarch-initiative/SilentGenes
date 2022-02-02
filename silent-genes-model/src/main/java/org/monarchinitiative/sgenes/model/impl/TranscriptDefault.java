package org.monarchinitiative.sgenes.model.impl;

import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.sgenes.model.base.BaseTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;

public class TranscriptDefault extends BaseTranscript {

    public static TranscriptDefault of(TranscriptIdentifier id,
                                       GenomicRegion location,
                                       List<Coordinates> exons) {
        return new TranscriptDefault(id, location, exons);
    }

    private TranscriptDefault(TranscriptIdentifier id, GenomicRegion location, List<Coordinates> exons) {
        super(id, location, exons);
    }

    @Override
    public String toString() {
        return "TranscriptDefault{} " + super.toString();
    }
}
