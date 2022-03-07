package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.RefseqTranscriptMetadata;
import org.monarchinitiative.sgenes.model.TranscriptEvidence;
import org.monarchinitiative.sgenes.model.base.BaseTranscriptMetadata;

import java.util.Objects;

public class RefseqTranscriptTranscriptMetadataImpl extends BaseTranscriptMetadata implements RefseqTranscriptMetadata {

    private final Biotype biotype;

    public static RefseqTranscriptTranscriptMetadataImpl of(TranscriptEvidence evidence, Biotype biotype) {
        return new RefseqTranscriptTranscriptMetadataImpl(evidence, biotype);
    }

    private RefseqTranscriptTranscriptMetadataImpl(TranscriptEvidence evidence, Biotype biotype) {
        super(evidence);
        this.biotype = Objects.requireNonNull(biotype, "Biotype must not be null");
    }

    @Override
    public Biotype biotype() {
        return biotype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(biotype);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefseqTranscriptTranscriptMetadataImpl that = (RefseqTranscriptTranscriptMetadataImpl) o;
        return biotype == that.biotype;
    }

    @Override
    public String toString() {
        return "RefseqMetadataImpl{" +
                "biotype=" + biotype +
                '}';
    }
}
