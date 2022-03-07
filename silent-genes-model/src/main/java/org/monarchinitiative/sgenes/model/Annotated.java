package org.monarchinitiative.sgenes.model;

import java.util.Optional;

public interface Annotated<T extends TranscriptMetadata> {

    T metadata();

    default Optional<FeatureSource> featureSource() {
        return metadata().featureSource();
    }

    default Optional<TranscriptEvidence> evidence() {
        return metadata().evidence();
    }

}
