package xyz.ielis.silent.genes.model.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.Transcript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;

abstract class BaseTranscript implements Transcript {

    private final TranscriptIdentifier id;
    private final GenomicRegion location;
    private final List<Coordinates> exons;

    protected BaseTranscript(TranscriptIdentifier id,
                             GenomicRegion location,
                             List<Coordinates> exons) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        this.location = Objects.requireNonNull(location, "Location must not be null");
        this.exons = Objects.requireNonNull(exons, "Exons must not be null");
        if (exons.isEmpty())
            throw new IllegalArgumentException("Exon list must not be empty");
    }

    @Override
    public TranscriptIdentifier id() {
        return id;
    }

    @Override
    public GenomicRegion location() {
        return location;
    }

    public List<Coordinates> exons() {
        return exons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTranscript that = (BaseTranscript) o;
        return Objects.equals(id, that.id) && Objects.equals(location, that.location) && Objects.equals(exons, that.exons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, exons);
    }

    @Override
    public String toString() {
        return "BaseTranscript{" +
                "id=" + id +
                ", location=" + location +
                ", exons=" + exons +
                '}';
    }
}
