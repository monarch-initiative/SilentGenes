package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.GeneIdentifier;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class GencodeGeneImpl implements GencodeGene {

    private final GeneIdentifier id;
    private final String type;
    private final EvidenceLevel evidenceLevel;
    private final GenomicRegion location;
    private final Set<GencodeTranscript> transcripts;

    public static GencodeGeneImpl of(GenomicRegion location,
                                     GeneIdentifier id,
                                     String type,
                                     EvidenceLevel evidenceLevel,
                                     Collection<GencodeTranscript> transcripts) {
        Objects.requireNonNull(location, "Location must not be null");
        Objects.requireNonNull(id, "ID must not be null");
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (transcripts.isEmpty()) {
            throw new IllegalArgumentException("Transcript collection must not be empty");
        }
        return new GencodeGeneImpl(id, type, evidenceLevel, location, Set.copyOf(transcripts));
    }

    private GencodeGeneImpl(GeneIdentifier id,
                            String type,
                            EvidenceLevel evidenceLevel,
                            GenomicRegion location,
                            Set<GencodeTranscript> transcripts) {
        this.id = id;
        this.type = type;
        this.evidenceLevel = evidenceLevel;
        this.location = location;
        this.transcripts = transcripts;
    }

    @Override
    public GeneIdentifier id() {
        return id;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public EvidenceLevel evidenceLevel() {
        return evidenceLevel;
    }

    @Override
    public GenomicRegion location() {
        return location;
    }

    @Override
    public Iterable<? extends GencodeTranscript> transcripts() {
        return transcripts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GencodeGeneImpl gene = (GencodeGeneImpl) o;
        return Objects.equals(id, gene.id) && Objects.equals(type, gene.type) && evidenceLevel == gene.evidenceLevel && Objects.equals(location, gene.location) && Objects.equals(transcripts, gene.transcripts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, evidenceLevel, location, transcripts);
    }

    @Override
    public String toString() {
        return "GeneImpl{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", evidenceLevel=" + evidenceLevel +
                ", location=" + location +
                ", transcripts=" + transcripts +
                '}';
    }
}
