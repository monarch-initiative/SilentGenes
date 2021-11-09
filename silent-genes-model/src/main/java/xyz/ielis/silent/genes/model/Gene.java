package xyz.ielis.silent.genes.model;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.impl.GeneDefault;

import java.util.Set;

/**
 * Gene is a region in a genome that has at least one transcript.
 */
public interface Gene extends Located, Spliced, Identified<GeneIdentifier> {

    static Gene of(GeneIdentifier id,
                   GenomicRegion location,
                   Set<? extends Transcript> transcripts) {
        return new GeneDefault(id, location, transcripts);
    }

}
