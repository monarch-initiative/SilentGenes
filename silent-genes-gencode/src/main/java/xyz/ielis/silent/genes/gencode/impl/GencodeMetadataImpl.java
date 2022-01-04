package xyz.ielis.silent.genes.gencode.impl;

import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeMetadata;

import java.util.Objects;
import java.util.Set;

public class GencodeMetadataImpl implements GencodeMetadata {

    private final Biotype biotype;
    private final EvidenceLevel evidenceLevel;
    private final Set<String> tags;

    public static GencodeMetadataImpl of(Biotype biotype, EvidenceLevel evidenceLevel, Set<String> tags) {
        return new GencodeMetadataImpl(biotype, evidenceLevel, tags);
    }

    private GencodeMetadataImpl(Biotype biotype, EvidenceLevel evidenceLevel, Set<String> tags) {
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
        GencodeMetadataImpl that = (GencodeMetadataImpl) o;
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
