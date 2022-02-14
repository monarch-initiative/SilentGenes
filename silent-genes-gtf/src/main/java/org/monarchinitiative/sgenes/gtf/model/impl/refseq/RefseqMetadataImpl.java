package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.RefseqMetadata;

import java.util.Objects;

public class RefseqMetadataImpl implements RefseqMetadata {

    private final Biotype biotype;

    RefseqMetadataImpl(Biotype biotype) {
        this.biotype = Objects.requireNonNull(biotype, "Biotype must not be null");
    }

    @Override
    public Biotype biotype() {
        return biotype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(biotype);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefseqMetadataImpl that = (RefseqMetadataImpl) o;
        return biotype == that.biotype;
    }

    @Override
    public String toString() {
        return "RefseqMetadataImpl{" +
                "biotype=" + biotype +
                '}';
    }

    public static RefseqMetadataImpl of(Biotype biotype) {
        return new RefseqMetadataImpl(biotype);
    }
}
