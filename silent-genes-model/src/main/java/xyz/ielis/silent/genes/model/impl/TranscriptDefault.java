package xyz.ielis.silent.genes.model.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

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
