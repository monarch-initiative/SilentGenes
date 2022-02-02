package xyz.ielis.silent.genes.model.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;
import xyz.ielis.silent.genes.model.base.BaseCodingTranscript;

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
