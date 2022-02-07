package org.monarchinitiative.sgenes.model.base;

import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.Objects;

public abstract class BaseGene implements Gene {

    protected final GeneIdentifier id;
    protected final GenomicRegion location;

    protected BaseGene(GeneIdentifier id, GenomicRegion location) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        this.location = Objects.requireNonNull(location, "Location must not be null");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseGene baseGene = (BaseGene) o;
        return Objects.equals(id, baseGene.id) && Objects.equals(location, baseGene.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location);
    }

    @Override
    public String toString() {
        return "BaseGene{" +
                "id=" + id +
                ", location=" + location +
                '}';
    }
}
