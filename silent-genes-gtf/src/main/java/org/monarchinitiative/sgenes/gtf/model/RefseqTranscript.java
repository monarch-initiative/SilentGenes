package org.monarchinitiative.sgenes.gtf.model;

import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqCodingTranscript;
import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqNoncodingTranscript;
import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;

public interface RefseqTranscript extends Transcript, RefseqAnnotated {

    static RefseqTranscript of(TranscriptIdentifier id,
                               GenomicRegion location,
                               List<Coordinates> exons,
                               RefseqSource source,
                               RefseqMetadata refseqMetadata,
                               Coordinates cdsCoordinates) {
        if (cdsCoordinates == null) {
            return RefseqNoncodingTranscript.of(id, location, exons, source, refseqMetadata);
        } else {
            return RefseqCodingTranscript.of(id, location, exons, source, refseqMetadata, cdsCoordinates);
        }
    }

    RefseqSource source();

}
