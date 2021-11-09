package xyz.ielis.silent.genes.model.impl;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.Gene;
import xyz.ielis.silent.genes.model.GeneIdentifier;
import xyz.ielis.silent.genes.model.Transcript;

import java.util.Objects;
import java.util.Set;

public class GeneDefault implements Gene {

    private final GeneIdentifier id;
    private final GenomicRegion location;
    private final Set<? extends Transcript> transcripts;

    public GeneDefault(GeneIdentifier id,
                       GenomicRegion location,
                       Set<? extends Transcript> transcripts) {
        this.id = id;
        this.location = location;
        this.transcripts = transcripts;
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
    public Iterable<? extends Transcript> transcripts() {
        return transcripts;
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
