package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.sgenes.gtf.model.RefseqMetadata;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.sgenes.model.base.BaseGene;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RefseqGeneImpl extends BaseGene implements RefseqGene {

    private final List<? extends Transcript> transcripts;
    private final RefseqMetadata refseqMetadata;

    public static RefseqGeneImpl of(GeneIdentifier identifier,
                                    GenomicRegion location,
                                    List<? extends Transcript> transcripts,
                                    RefseqMetadata refseqMetadata) {
        return new RefseqGeneImpl(identifier, location, transcripts, refseqMetadata);
    }

    private RefseqGeneImpl(GeneIdentifier identifier,
                   GenomicRegion location,
                   List<? extends Transcript> transcripts,
                   RefseqMetadata refseqMetadata) {
        super(identifier, location);
        this.transcripts = Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (this.transcripts.isEmpty())
            throw new IllegalArgumentException("Transcripts must not be empty");
        this.refseqMetadata = Objects.requireNonNull(refseqMetadata, "Refseq metadata must not be empty");
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
    public RefseqMetadata refseqMetadata() {
        return refseqMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RefseqGeneImpl that = (RefseqGeneImpl) o;
        return Objects.equals(transcripts, that.transcripts) && Objects.equals(refseqMetadata, that.refseqMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transcripts, refseqMetadata);
    }

    @Override
    public String toString() {
        return "RefseqGeneImpl{" +
                "id=" + id +
                ", location=" + location +
                ", transcripts=" + transcripts +
                ", refseqMetadata=" + refseqMetadata +
                "} " + super.toString();
    }
}
