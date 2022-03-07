package org.monarchinitiative.sgenes.model.impl;

import org.monarchinitiative.sgenes.model.FeatureSource;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.sgenes.model.base.BaseGene;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.*;
import java.util.stream.Collectors;

public class GeneDefault extends BaseGene {

    private final List<? extends Transcript> transcripts;

    public GeneDefault(GeneIdentifier id,
                       GenomicRegion location,
                       List<? extends Transcript> transcripts) {
        super(id, location);
        this.transcripts = Objects.requireNonNull(transcripts, "Transcripts must not be null");
        check();
    }

    private void check() {
        if (transcripts.isEmpty())
            throw new IllegalStateException("Transcript list must not be empty");
        List<FeatureSource> evidences = transcripts.stream()
                .map(Transcript::featureSource)
                .flatMap(Optional::stream)
                .distinct()
                .collect(Collectors.toList());
        if (evidences.size() > 1)
            throw new IllegalStateException("Gene must not have transcripts defined by multiple sources");
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
