package xyz.ielis.silent.genes.model.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.CodingTranscript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;

public class CodingTranscriptDefault extends BaseTranscript implements CodingTranscript {

    private final Coordinates cdsCoordinates;

    public static CodingTranscriptDefault of(TranscriptIdentifier id,
                                             GenomicRegion location,
                                             List<Coordinates> exons,
                                             Coordinates cdsCoordinates) {
        return new CodingTranscriptDefault(id, location, exons, cdsCoordinates);
    }

    private CodingTranscriptDefault(TranscriptIdentifier id,
                                    GenomicRegion location,
                                    List<Coordinates> exons, Coordinates cdsCoordinates) {
        super(id, location, exons);
        this.cdsCoordinates = cdsCoordinates;
    }

    @Override
    public Coordinates cdsCoordinates() {
        return cdsCoordinates;
    }
}
