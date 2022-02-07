package org.monarchinitiative.sgenes.model.impl;

import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.sgenes.model.base.BaseGene;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.*;

public class GeneDefault extends BaseGene {

    private final List<? extends Transcript> transcripts;

    public GeneDefault(GeneIdentifier id,
                       GenomicRegion location,
                       List<? extends Transcript> transcripts) {
        super(id, location);
        this.transcripts = Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (this.transcripts.isEmpty())
            throw new IllegalArgumentException("Transcripts must not be empty");
    }

    @Override
    public Iterator<? extends Transcript> transcripts() {
        return transcripts.iterator();
    }

    @Override
    public int transcriptCount() {
        return transcripts.size();
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
