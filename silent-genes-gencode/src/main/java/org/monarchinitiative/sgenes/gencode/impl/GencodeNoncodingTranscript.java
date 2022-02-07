package org.monarchinitiative.sgenes.gencode.impl;

import org.monarchinitiative.sgenes.gencode.model.GencodeMetadata;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;

import java.util.List;

public class GencodeNoncodingTranscript extends GencodeBaseTranscript {

    private GencodeNoncodingTranscript(TranscriptIdentifier id,
                                       GenomicRegion location,
                                       List<Coordinates> exons,
                                       GencodeMetadata gencodeMetadata) {
        super(id, location, exons, gencodeMetadata);
    }

    public static GencodeNoncodingTranscript of(TranscriptIdentifier id,
                                                GenomicRegion location,
                                                List<Coordinates> exons,
                                                GencodeMetadata gencodeMetadata) {
        return new GencodeNoncodingTranscript(id, location, exons, gencodeMetadata);
    }

    @Override
    public String toString() {
        return "GencodeNoncodingTranscript{} " + super.toString();
    }
}
