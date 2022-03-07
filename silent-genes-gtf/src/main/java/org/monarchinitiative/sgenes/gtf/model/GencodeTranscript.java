package org.monarchinitiative.sgenes.gtf.model;


import org.monarchinitiative.sgenes.gtf.model.impl.gencode.GencodeCodingTranscript;
import org.monarchinitiative.sgenes.gtf.model.impl.gencode.GencodeNoncodingTranscript;
import org.monarchinitiative.sgenes.model.Transcript;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Set;

public interface GencodeTranscript extends Transcript {

    static GencodeTranscript of(TranscriptIdentifier id,
                                GenomicRegion location,
                                List<Coordinates> exons,
                                GencodeTranscriptMetadata metadata,
                                Coordinates cdsCoordinates) {
        if (cdsCoordinates == null) {
            return GencodeNoncodingTranscript.of(id, location, exons, metadata);
        } else {
            return GencodeCodingTranscript.of(id, location, exons, cdsCoordinates, metadata);
        }
    }

    Biotype biotype();

    EvidenceLevel evidenceLevel();

    Set<String> tags();

}
