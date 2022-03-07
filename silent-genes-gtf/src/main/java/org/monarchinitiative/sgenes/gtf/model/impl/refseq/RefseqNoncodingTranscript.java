package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.gtf.model.*;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.sgenes.model.base.BaseTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Objects;

public class RefseqNoncodingTranscript extends BaseTranscript implements RefseqTranscript {

    private final RefseqSource source;

    public static RefseqNoncodingTranscript of(TranscriptIdentifier id,
                                               GenomicRegion location,
                                               List<Coordinates> exons,
                                               RefseqSource source,
                                               RefseqTranscriptMetadata metadata) {
        return new RefseqNoncodingTranscript(id, location, exons, source, metadata);
    }

    protected RefseqNoncodingTranscript(TranscriptIdentifier id,
                                        GenomicRegion location,
                                        List<Coordinates> exons,
                                        RefseqSource source,
                                        RefseqTranscriptMetadata metadata) {
        super(id, location, exons, metadata);
        this.source = Objects.requireNonNull(source, "Refseq source must not be null");
    }

    @Override
    public RefseqSource source() {
        return source;
    }

    @Override
    public Biotype biotype() {
        return ((RefseqTranscriptMetadata) metadata()).biotype();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RefseqNoncodingTranscript that = (RefseqNoncodingTranscript) o;
        return source == that.source;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source);
    }

    @Override
    public String toString() {
        return "RefseqNoncodingTranscript{" +
                "id=" + id() +
                ", source=" + source +
                ", location=" + location() +
                ", exons=" + exons() +
                ", metadata=" + metadata() +
                "}";
    }
}
