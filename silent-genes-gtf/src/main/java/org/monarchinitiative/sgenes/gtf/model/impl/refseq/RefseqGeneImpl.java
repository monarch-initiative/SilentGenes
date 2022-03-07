package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.sgenes.gtf.model.RefseqTranscript;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.base.BaseGene;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RefseqGeneImpl extends BaseGene implements RefseqGene {

    private final List<? extends RefseqTranscript> transcripts;
    private final Biotype biotype;

    public static RefseqGeneImpl of(GeneIdentifier identifier,
                                    GenomicRegion location,
                                    List<? extends RefseqTranscript> transcripts,
                                    Biotype biotype) {
        return new RefseqGeneImpl(identifier, location, transcripts, biotype);
    }

    private RefseqGeneImpl(GeneIdentifier identifier,
                   GenomicRegion location,
                   List<? extends RefseqTranscript> transcripts,
                   Biotype biotype) {
        super(identifier, location);
        this.transcripts = Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (this.transcripts.isEmpty())
            throw new IllegalArgumentException("Transcripts must not be empty");
        this.biotype = Objects.requireNonNull(biotype, "Biotype must not be empty");
    }

    @Override
    public Iterator<? extends RefseqTranscript> transcripts() {
        return transcripts.iterator();
    }

    @Override
    public int transcriptCount() {
        return transcripts.size();
    }

    @Override
    public Biotype biotype() {
        return biotype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RefseqGeneImpl that = (RefseqGeneImpl) o;
        return Objects.equals(transcripts, that.transcripts) && Objects.equals(biotype, that.biotype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transcripts, biotype);
    }

    @Override
    public String toString() {
        return "RefseqGeneImpl{" +
                "id=" + id +
                ", location=" + location +
                ", transcripts=" + transcripts +
                ", biotype=" + biotype +
                "}";
    }
}
