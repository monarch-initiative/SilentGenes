package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.GeneIdentifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class GencodeGeneImpl implements GencodeGene {

    private final GeneIdentifier id;
    private final Biotype biotype;
    private final EvidenceLevel evidenceLevel;
    private final GenomicRegion location;
    private final Set<GencodeTranscript> transcripts;
    private final Set<String> tags;

    public static GencodeGeneImpl of(GenomicRegion location,
                                     GeneIdentifier id,
                                     Biotype biotype,
                                     EvidenceLevel evidenceLevel,
                                     Collection<GencodeTranscript> transcripts,
                                     Collection<String> tags) {
        Objects.requireNonNull(location, "Location must not be null");
        Objects.requireNonNull(id, "ID must not be null");
        Objects.requireNonNull(biotype, "Biotype must not be null");
        Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (transcripts.isEmpty()) {
            throw new IllegalArgumentException("Transcript collection must not be empty");
        }
        Objects.requireNonNull(tags, "Tags must not be empty");
        return new GencodeGeneImpl(id, biotype, evidenceLevel, location, Set.copyOf(transcripts), Set.copyOf(tags));
    }

    private GencodeGeneImpl(GeneIdentifier id,
                            Biotype biotype,
                            EvidenceLevel evidenceLevel,
                            GenomicRegion location,
                            Set<GencodeTranscript> transcripts,
                            Set<String> tags) {
        this.id = id;
        this.biotype = biotype;
        this.evidenceLevel = evidenceLevel;
        this.location = location;
        this.transcripts = transcripts;
        this.tags = tags;
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
    public Iterator<? extends GencodeTranscript> transcripts() {
        return transcripts.iterator();
    }

    @Override
    public int transcriptCount() {
        return transcripts.size();
    }

    @Override
    public Set<String> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GencodeGeneImpl that = (GencodeGeneImpl) o;
        return Objects.equals(id, that.id) && biotype == that.biotype && evidenceLevel == that.evidenceLevel && Objects.equals(location, that.location) && Objects.equals(transcripts, that.transcripts) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, biotype, evidenceLevel, location, transcripts, tags);
    }

    @Override
    public String toString() {
        return "GencodeGeneImpl{" +
                "id=" + id +
                ", biotype=" + biotype +
                ", evidenceLevel=" + evidenceLevel +
                ", location=" + location +
                ", transcripts=" + transcripts +
                ", tags=" + tags +
                '}';
    }
}
