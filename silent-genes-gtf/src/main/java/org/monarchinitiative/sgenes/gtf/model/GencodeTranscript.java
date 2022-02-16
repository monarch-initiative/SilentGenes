package org.monarchinitiative.sgenes.gtf.model;


import org.monarchinitiative.sgenes.gtf.model.impl.gencode.GencodeCodingTranscript;
import org.monarchinitiative.sgenes.gtf.model.impl.gencode.GencodeNoncodingTranscript;
import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;

public interface GencodeTranscript extends Transcript, GencodeAnnotated {

    static GencodeTranscript of(TranscriptIdentifier id,
                                GenomicRegion location,
                                List<Coordinates> exons,
                                GencodeMetadata gencodeMetadata,
                                Coordinates cdsCoordinates) {
        if (cdsCoordinates == null) {
            return GencodeNoncodingTranscript.of(id, location, exons, gencodeMetadata);
        } else {
            return GencodeCodingTranscript.of(id, location, exons, cdsCoordinates, gencodeMetadata);
        }
    }

}
