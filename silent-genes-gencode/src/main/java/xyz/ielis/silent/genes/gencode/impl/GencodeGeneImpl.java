package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;
import xyz.ielis.silent.genes.gencode.model.GencodeTranscript;
import xyz.ielis.silent.genes.model.GeneIdentifier;
import xyz.ielis.silent.genes.model.impl.GeneDefault;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class GencodeGeneImpl extends GeneDefault implements GencodeGene {

    private final Biotype biotype;
    private final EvidenceLevel evidenceLevel;
    private final Set<String> tags;

    public static GencodeGeneImpl of(GenomicRegion location,
                                     GeneIdentifier id,
                                     Biotype biotype,
                                     EvidenceLevel evidenceLevel,
                                     Collection<GencodeTranscript> transcripts,
                                     Collection<String> tags) {
        return new GencodeGeneImpl(id, biotype, evidenceLevel, location, Set.copyOf(transcripts), Set.copyOf(tags));
    }

    private GencodeGeneImpl(GeneIdentifier id,
                            Biotype biotype,
                            EvidenceLevel evidenceLevel,
                            GenomicRegion location,
                            Set<GencodeTranscript> transcripts,
                            Set<String> tags) {
        super(id, location, transcripts);
        this.biotype = Objects.requireNonNull(biotype, "Biotype must not be null");
        this.evidenceLevel = Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        this.tags = Objects.requireNonNull(tags, "Tags must not be empty");
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
        GencodeGeneImpl that = (GencodeGeneImpl) o;
        return biotype == that.biotype && evidenceLevel == that.evidenceLevel && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), biotype, evidenceLevel, tags);
    }

    @Override
    public String toString() {
        return "GencodeGeneImpl{" +
                "biotype=" + biotype +
                ", evidenceLevel=" + evidenceLevel +
                ", tags=" + tags +
                '}';
    }
}
