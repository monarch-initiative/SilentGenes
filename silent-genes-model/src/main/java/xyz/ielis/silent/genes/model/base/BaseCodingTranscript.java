package xyz.ielis.silent.genes.model.base;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.CodingTranscript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;

public abstract class BaseCodingTranscript extends BaseTranscript implements CodingTranscript {

    private final Coordinates cdsCoordinates;

    protected BaseCodingTranscript(TranscriptIdentifier id,
                                   GenomicRegion location,
                                   List<Coordinates> exons,
                                   Coordinates cdsCoordinates) {
        super(id, location, exons);
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
        BaseCodingTranscript that = (BaseCodingTranscript) o;
        return Objects.equals(cdsCoordinates, that.cdsCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cdsCoordinates);
    }

    @Override
    public String toString() {
        return "BaseCodingTranscript{" +
                "cdsCoordinates=" + cdsCoordinates +
                "} " + super.toString();
    }
}
