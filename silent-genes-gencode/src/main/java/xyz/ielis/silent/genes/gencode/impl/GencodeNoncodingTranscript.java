package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GencodeNoncodingTranscript extends GencodeBaseTranscript {

    private GencodeNoncodingTranscript(GenomicRegion location,
                                       TranscriptIdentifier id,
                                       Biotype biotype,
                                       EvidenceLevel evidenceLevel,
                                       List<Coordinates> exons,
                                       Set<String> tags) {
        super(location, id, biotype, evidenceLevel, exons, tags);
    }

    public static GencodeNoncodingTranscript of(GenomicRegion location,
                                                TranscriptIdentifier id,
                                                Biotype biotype,
                                                EvidenceLevel evidenceLevel,
                                                List<Coordinates> exons,
                                                Set<String> tags) {
        return new GencodeNoncodingTranscript(location, id, biotype, evidenceLevel, exons, tags);
    }

    @Override
    public String toString() {
        return "NoncodingTranscript{} " + super.toString();
    }
}
