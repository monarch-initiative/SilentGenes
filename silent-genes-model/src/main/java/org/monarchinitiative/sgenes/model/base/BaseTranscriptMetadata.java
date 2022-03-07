package org.monarchinitiative.sgenes.model.base;

import org.monarchinitiative.sgenes.model.TranscriptEvidence;
import org.monarchinitiative.sgenes.model.TranscriptMetadata;

import java.util.Objects;
import java.util.Optional;

public class BaseTranscriptMetadata implements TranscriptMetadata {

    private final TranscriptEvidence evidence;

    public static BaseTranscriptMetadata of(TranscriptEvidence evidence) {
        return new BaseTranscriptMetadata(evidence);
    }

    protected BaseTranscriptMetadata(TranscriptEvidence evidence) {
        this.evidence = evidence; // nullable
    }

    @Override
    public Optional<TranscriptEvidence> evidence() {
        return Optional.ofNullable(evidence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTranscriptMetadata that = (BaseTranscriptMetadata) o;
        return evidence == that.evidence;
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidence);
    }

    @Override
    public String toString() {
        return "BaseTranscriptMetadata{" +
                "evidence=" + evidence +
                '}';
    }
}
