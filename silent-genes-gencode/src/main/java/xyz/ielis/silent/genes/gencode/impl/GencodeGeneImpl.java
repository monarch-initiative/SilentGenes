package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.GeneIdentifier;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class GencodeGeneImpl implements GencodeGene {

    private final GeneIdentifier id;
    private final Biotype biotype;
    private final EvidenceLevel evidenceLevel;
    private final GenomicRegion location;
    private final Set<GencodeTranscript> transcripts;

    public static GencodeGeneImpl of(GenomicRegion location,
                                     GeneIdentifier id,
                                     Biotype biotype,
                                     EvidenceLevel evidenceLevel,
                                     Collection<GencodeTranscript> transcripts) {
        Objects.requireNonNull(location, "Location must not be null");
        Objects.requireNonNull(id, "ID must not be null");
        Objects.requireNonNull(biotype, "Biotype must not be null");
        Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (transcripts.isEmpty()) {
            throw new IllegalArgumentException("Transcript collection must not be empty");
        }
        return new GencodeGeneImpl(id, biotype, evidenceLevel, location, Set.copyOf(transcripts));
    }

    private GencodeGeneImpl(GeneIdentifier id,
                            Biotype biotype,
                            EvidenceLevel evidenceLevel,
                            GenomicRegion location,
                            Set<GencodeTranscript> transcripts) {
        this.id = id;
        this.biotype = biotype;
        this.evidenceLevel = evidenceLevel;
        this.location = location;
        this.transcripts = transcripts;
    }

    @Override
    public GeneIdentifier id() {
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
    public GenomicRegion location() {
        return location;
    }

    @Override
    public Stream<? extends GencodeTranscript> transcripts() {
        return transcripts.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GencodeGeneImpl gene = (GencodeGeneImpl) o;
        return Objects.equals(id, gene.id) && Objects.equals(biotype, gene.biotype) && evidenceLevel == gene.evidenceLevel && Objects.equals(location, gene.location) && Objects.equals(transcripts, gene.transcripts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, biotype, evidenceLevel, location, transcripts);
    }

    @Override
    public String toString() {
        return "GeneImpl{" +
                "id='" + id + '\'' +
                ", biotype='" + biotype + '\'' +
                ", evidenceLevel=" + evidenceLevel +
                ", location=" + location +
                ", transcripts=" + transcripts +
                '}';
    }
}
