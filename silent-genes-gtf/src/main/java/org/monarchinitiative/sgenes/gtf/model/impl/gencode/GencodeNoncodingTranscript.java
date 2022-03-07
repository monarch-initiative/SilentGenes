package org.monarchinitiative.sgenes.gtf.model.impl.gencode;

import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.EvidenceLevel;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscriptMetadata;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscript;
import org.monarchinitiative.sgenes.model.base.BaseTranscript;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Set;

public class GencodeNoncodingTranscript extends BaseTranscript implements GencodeTranscript {

    public static GencodeNoncodingTranscript of(TranscriptIdentifier id,
                                                GenomicRegion location,
                                                List<Coordinates> exons,
                                                GencodeTranscriptMetadata gencodeTranscriptMetadata) {
        return new GencodeNoncodingTranscript(id, location, exons, gencodeTranscriptMetadata);
    }

    GencodeNoncodingTranscript(TranscriptIdentifier id,
                               GenomicRegion location,
                               List<Coordinates> exons,
                               GencodeTranscriptMetadata gencodeTranscriptMetadata) {
        super(id, location, exons, gencodeTranscriptMetadata);
    }

    @Override
    public Biotype biotype() {
        return ((GencodeTranscriptMetadata) metadata()).biotype();
    }

    @Override
    public EvidenceLevel evidenceLevel() {
        return ((GencodeTranscriptMetadata) metadata()).evidenceLevel();
    }

    @Override
    public Set<String> tags() {
        return ((GencodeTranscriptMetadata) metadata()).tags();
    }

    @Override
    public String toString() {
        return "GencodeNoncodingTranscript{" +
                "id=" + id() +
                ", metadata=" + metadata() +
                ", location=" + location() +
                ", exons=" + exons() +
                "}";
    }
}
