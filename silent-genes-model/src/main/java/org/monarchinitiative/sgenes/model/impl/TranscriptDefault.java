package org.monarchinitiative.sgenes.model.impl;

import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.sgenes.model.TranscriptMetadata;
import org.monarchinitiative.sgenes.model.base.BaseTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;

public class TranscriptDefault extends BaseTranscript {

    public static TranscriptDefault of(TranscriptIdentifier id,
                                       GenomicRegion location,
                                       List<Coordinates> exons,
                                       TranscriptMetadata metadata) {
        return new TranscriptDefault(id, location, exons, metadata);
    }

    private TranscriptDefault(TranscriptIdentifier id,
                              GenomicRegion location,
                              List<Coordinates> exons,
                              TranscriptMetadata metadata) {
        super(id, location, exons, metadata);
    }

    @Override
    public String toString() {
        return "TranscriptDefault{" +
                "id=" + id() +
                ", location=" + location() +
                ", exons=" + exons() +
                '}';
    }
}
