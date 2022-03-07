package org.monarchinitiative.sgenes.gtf.model.impl.gencode;

import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.EvidenceLevel;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscriptMetadata;
import org.monarchinitiative.sgenes.model.TranscriptEvidence;
import org.monarchinitiative.sgenes.model.base.BaseTranscriptMetadata;

import java.util.Objects;
import java.util.Set;

public class GencodeTranscriptTranscriptMetadataImpl extends BaseTranscriptMetadata implements GencodeTranscriptMetadata {

    private final Biotype biotype;
    private final EvidenceLevel evidenceLevel;
    private final Set<String> tags;

    public static GencodeTranscriptTranscriptMetadataImpl of(TranscriptEvidence evidence, Biotype biotype, EvidenceLevel evidenceLevel, Set<String> tags) {
        return new GencodeTranscriptTranscriptMetadataImpl(evidence, biotype, evidenceLevel, tags);
    }

    private GencodeTranscriptTranscriptMetadataImpl(TranscriptEvidence evidence, Biotype biotype, EvidenceLevel evidenceLevel, Set<String> tags) {
        super(evidence);
        this.biotype = Objects.requireNonNull(biotype, "Biotype must not be null");
        this.evidenceLevel = Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        this.tags = Objects.requireNonNull(tags, "Tags must not be null");
    }

    @Override
    public Biotype biotype() {
        return biotype;
    }

    @Override
    public EvidenceLevel evidenceLevel() {
        return evidenceLevel;
    }

    @Override
    public Set<String> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GencodeTranscriptTranscriptMetadataImpl that = (GencodeTranscriptTranscriptMetadataImpl) o;
        return biotype == that.biotype && evidenceLevel == that.evidenceLevel && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(biotype, evidenceLevel, tags);
    }

    @Override
    public String toString() {
        return "GencodeMetadataImpl{" +
                "biotype=" + biotype +
                ", evidenceLevel=" + evidenceLevel +
                ", tags=" + tags +
                '}';
    }
}
