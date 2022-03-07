package org.monarchinitiative.sgenes.gtf.model;

import org.monarchinitiative.sgenes.gtf.model.impl.gencode.GencodeTranscriptTranscriptMetadataImpl;
import org.monarchinitiative.sgenes.model.TranscriptEvidence;
import org.monarchinitiative.sgenes.model.TranscriptMetadata;

import java.util.Set;

public interface GencodeTranscriptMetadata extends TranscriptMetadata {

    static GencodeTranscriptMetadata of(TranscriptEvidence evidence, Biotype biotype, EvidenceLevel evidenceLevel, Set<String> tags) {
        return GencodeTranscriptTranscriptMetadataImpl.of(evidence, biotype, evidenceLevel, tags);
    }

    Biotype biotype();

    EvidenceLevel evidenceLevel();

    Set<String> tags();

}
