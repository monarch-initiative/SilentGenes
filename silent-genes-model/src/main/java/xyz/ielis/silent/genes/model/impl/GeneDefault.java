package xyz.ielis.silent.genes.model.impl;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.Gene;
import xyz.ielis.silent.genes.model.GeneIdentifier;
import xyz.ielis.silent.genes.model.Transcript;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class GeneDefault implements Gene {

    private final GeneIdentifier id;
    private final GenomicRegion location;
    private final Set<? extends Transcript> transcripts;

    public GeneDefault(GeneIdentifier id,
                       GenomicRegion location,
                       Collection<? extends Transcript> transcripts) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        this.location = Objects.requireNonNull(location, "Location must not be null");
        this.transcripts = Set.copyOf(Objects.requireNonNull(transcripts, "Transcripts must not be null"));
        if (this.transcripts.isEmpty())
            throw new IllegalArgumentException("Transcripts must not be empty");
    }

    @Override
    public GeneIdentifier id() {
        return id;
    }

    @Override
    public GenomicRegion location() {
        return location;
    }

    @Override
    public Stream<? extends Transcript> transcripts() {
        return transcripts.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneDefault that = (GeneDefault) o;
        return Objects.equals(id, that.id) && Objects.equals(location, that.location) && Objects.equals(transcripts, that.transcripts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, transcripts);
    }

    @Override
    public String toString() {
        return "GeneDefault{" +
                "id=" + id +
                ", location=" + location +
                ", transcripts=" + transcripts +
                '}';
    }
}
