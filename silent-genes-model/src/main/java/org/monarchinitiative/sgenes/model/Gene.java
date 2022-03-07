package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.sgenes.model.impl.GeneDefault;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Optional;

/**
 * Gene is a region in a genome that has at least one transcript.
 */
public interface Gene extends Located, Spliced, Identified<GeneIdentifier> {

    static Gene of(GeneIdentifier id,
                   GenomicRegion location,
                   List<? extends Transcript> transcripts) {
        return new GeneDefault(id, location, transcripts);
    }

    /**
     * @return optional with {@link FeatureSource} or an empty optional
     * if no transcript defines a {@link FeatureSource}
     */
    default Optional<FeatureSource> featureSource() {
        return transcriptStream()
                .map(Transcript::featureSource)
                .flatMap(Optional::stream)
                .findAny();
    }
}
