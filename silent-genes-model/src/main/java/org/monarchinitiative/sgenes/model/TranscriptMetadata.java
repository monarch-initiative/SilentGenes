package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.sgenes.model.base.BaseTranscriptMetadata;

import java.util.Optional;

public interface TranscriptMetadata {

    static TranscriptMetadata of(TranscriptEvidence evidence) {
        return BaseTranscriptMetadata.of(evidence);
    }

    Optional<TranscriptEvidence> evidence();

    default Optional<FeatureSource> featureSource() {
        return evidence().map(TranscriptEvidence::source);
    }

}
