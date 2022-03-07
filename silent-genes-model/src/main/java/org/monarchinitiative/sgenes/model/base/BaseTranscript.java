package org.monarchinitiative.sgenes.model.base;

import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.sgenes.model.TranscriptMetadata;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Objects;

public abstract class BaseTranscript implements Transcript {

    private final TranscriptIdentifier id;
    private final GenomicRegion location;
    private final List<Coordinates> exons;
    private final TranscriptMetadata metadata;

    protected BaseTranscript(TranscriptIdentifier id,
                             GenomicRegion location,
                             List<Coordinates> exons,
                             TranscriptMetadata metadata) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        this.location = Objects.requireNonNull(location, "Location must not be null");
        this.exons = exons; // non-nullity checked in Transcript.of()
        this.metadata = Objects.requireNonNull(metadata, "Metadata must not be null");
    }

    @Override
    public TranscriptIdentifier id() {
        return id;
    }

    @Override
    public TranscriptMetadata metadata() {
        return metadata;
    }

    @Override
    public GenomicRegion location() {
        return location;
    }

    public List<Coordinates> exons() {
        return exons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTranscript that = (BaseTranscript) o;
        return Objects.equals(id, that.id) && Objects.equals(location, that.location) && Objects.equals(exons, that.exons) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, exons, metadata);
    }

    @Override
    public String toString() {
        return "BaseTranscript{" +
                "id=" + id +
                ", metadata=" + metadata +
                ", location=" + location +
                ", exons=" + exons +
                '}';
    }
}
