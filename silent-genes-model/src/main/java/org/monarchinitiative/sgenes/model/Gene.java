package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.impl.GeneDefault;

import java.util.List;

/**
 * Gene is a region in a genome that has at least one transcript.
 */
public interface Gene extends Located, Spliced, Identified<GeneIdentifier> {

    static Gene of(GeneIdentifier id,
                   GenomicRegion location,
                   List<? extends Transcript> transcripts) {
        return new GeneDefault(id, location, transcripts);
    }

}
