package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;
import java.util.Set;

abstract class BaseTranscript implements GencodeTranscript {

    private final GenomicRegion location;
    private final TranscriptIdentifier id;
    private final Biotype biotype;
    private final EvidenceLevel evidenceLevel;
    private final List<Coordinates> exons;
    private final Set<String> tags;

    protected BaseTranscript(GenomicRegion location,
                             TranscriptIdentifier id,
                             Biotype biotype,
                             EvidenceLevel evidenceLevel,
                             List<Coordinates> exons,
                             Set<String> tags) {
        this.location = location;
        this.id = id;
        this.biotype = biotype;
        this.evidenceLevel = evidenceLevel;
        this.exons = exons;
        this.tags = tags;
    }


    @Override
    public GenomicRegion location() {
        return location;
    }

    @Override
    public TranscriptIdentifier id() {
        return id;
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
    public List<Coordinates> exons() {
        return exons;
    }

    @Override
    public Set<String> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTranscript that = (BaseTranscript) o;
        return Objects.equals(location, that.location) && Objects.equals(id, that.id) && biotype == that.biotype && evidenceLevel == that.evidenceLevel && Objects.equals(exons, that.exons) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, id, biotype, evidenceLevel, exons, tags);
    }

    @Override
    public String toString() {
        return "BaseTranscript{" +
                "location=" + location +
                ", id=" + id +
                ", biotype=" + biotype +
                ", evidenceLevel=" + evidenceLevel +
                ", exons=" + exons +
                ", tags=" + tags +
                '}';
    }

}
