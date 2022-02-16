package org.monarchinitiative.sgenes.gtf.model;

import org.monarchinitiative.sgenes.gtf.model.impl.gencode.GencodeMetadataImpl;

import java.util.Set;

public interface GencodeMetadata {

    static GencodeMetadata of(Biotype biotype, EvidenceLevel evidenceLevel, Set<String> tags) {
        return GencodeMetadataImpl.of(biotype, evidenceLevel, tags);
    }

    Biotype biotype();

    EvidenceLevel evidenceLevel();

    Set<String> tags();

}
