package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;
import xyz.ielis.silent.genes.model.impl.BaseTranscript;

import java.util.List;
import java.util.Objects;
import java.util.Set;

abstract class GencodeBaseTranscript extends BaseTranscript implements GencodeTranscript {

    private final Biotype biotype;
    private final EvidenceLevel evidenceLevel;
    private final Set<String> tags;

    protected GencodeBaseTranscript(GenomicRegion location,
                                    TranscriptIdentifier id,
                                    Biotype biotype,
                                    EvidenceLevel evidenceLevel,
                                    List<Coordinates> exons,
                                    Set<String> tags) {
        super(id, location, exons);
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
        if (!super.equals(o)) return false;
        GencodeBaseTranscript that = (GencodeBaseTranscript) o;
        return biotype == that.biotype && evidenceLevel == that.evidenceLevel && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), biotype, evidenceLevel, tags);
    }

    @Override
    public String toString() {
        return "GencodeBaseTranscript{" +
                "biotype=" + biotype +
                ", evidenceLevel=" + evidenceLevel +
                ", tags=" + tags +
                '}';
    }
}
