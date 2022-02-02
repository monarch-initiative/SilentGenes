package org.monarchinitiative.sgenes.gencode.model;

import org.monarchinitiative.sgenes.gencode.impl.GencodeMetadataImpl;

import java.util.Set;

public interface GencodeMetadata {

    static GencodeMetadata of(Biotype biotype, EvidenceLevel evidenceLevel, Set<String> tags) {
        return GencodeMetadataImpl.of(biotype, evidenceLevel, tags);
    }

    Biotype biotype();

    EvidenceLevel evidenceLevel();

    Set<String> tags();

}
