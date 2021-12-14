package xyz.ielis.silent.genes.model.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.Coding;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;

public class CodingTranscript extends BaseTranscript implements Coding {

    private final Coordinates startCodon, stopCodon;

    private final Coordinates fivePrimeCoordinates, threePrimeCoordinates;

    public static CodingTranscript of(TranscriptIdentifier id,
                                      GenomicRegion location,
                                      List<Coordinates> exons,
                                      Coordinates startCodon,
                                      Coordinates stopCodon, Coordinates fivePrimeCoordinates,
                                      Coordinates threePrimeCoordinates) {
        return new CodingTranscript(id, location, exons, startCodon, stopCodon, fivePrimeCoordinates, threePrimeCoordinates);
    }

    private CodingTranscript(TranscriptIdentifier id,
                             GenomicRegion location,
                             List<Coordinates> exons,
                             Coordinates startCodon,
                             Coordinates stopCodon, Coordinates fivePrimeCoordinates,
                             Coordinates threePrimeCoordinates) {
        super(id, location, exons);
        this.startCodon = startCodon;
        this.stopCodon = stopCodon;
        this.fivePrimeCoordinates = fivePrimeCoordinates;
        this.threePrimeCoordinates = threePrimeCoordinates;

    }

    @Override
    public Coordinates startCodon() {
        return startCodon;
    }

    @Override
    public Coordinates stopCodon() {
        return stopCodon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CodingTranscript that = (CodingTranscript) o;
        return Objects.equals(startCodon, that.startCodon) && Objects.equals(stopCodon, that.stopCodon) &&
                Objects.equals(fivePrimeCoordinates, that.fivePrimeCoordinates) &&
                Objects.equals(threePrimeCoordinates, that.threePrimeCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startCodon, stopCodon, fivePrimeCoordinates, threePrimeCoordinates);
    }

    @Override
    public String toString() {
        return "CodingTranscript{" +
                "startCodon=" + startCodon +
                ", stopCodon=" + stopCodon +
                "} " + super.toString();
    }
}
