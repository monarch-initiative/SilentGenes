package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.RefseqMetadata;
import org.monarchinitiative.sgenes.gtf.model.RefseqSource;
import org.monarchinitiative.sgenes.gtf.model.RefseqTranscript;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.sgenes.model.base.BaseTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Objects;

public class RefseqNoncodingTranscript extends BaseTranscript implements RefseqTranscript {

    private final RefseqSource source;
    private final RefseqMetadata refseqMetadata;

    public static RefseqNoncodingTranscript of(TranscriptIdentifier id,
                                               GenomicRegion location,
                                               List<Coordinates> exons,
                                               RefseqSource source,
                                               RefseqMetadata refseqMetadata) {
        return new RefseqNoncodingTranscript(id, location, exons, source, refseqMetadata);
    }

    protected RefseqNoncodingTranscript(TranscriptIdentifier id,
                                        GenomicRegion location,
                                        List<Coordinates> exons,
                                        RefseqSource source,
                                        RefseqMetadata refseqMetadata) {
        super(id, location, exons);
        this.source = Objects.requireNonNull(source, "Refseq source must not be null");
        this.refseqMetadata = Objects.requireNonNull(refseqMetadata, "Refseq metadata must not be null");
    }

    @Override
    public RefseqSource source() {
        return source;
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
        RefseqNoncodingTranscript that = (RefseqNoncodingTranscript) o;
        return source == that.source && Objects.equals(refseqMetadata, that.refseqMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source, refseqMetadata);
    }

    @Override
    public String toString() {
        return "RefseqNoncodingTranscript{" +
                "id=" + id() +
                ", source=" + source +
                ", location=" + location() +
                ", exons=" + exons() +
                ", refseqMetadata=" + refseqMetadata +
                "}";
    }
}
