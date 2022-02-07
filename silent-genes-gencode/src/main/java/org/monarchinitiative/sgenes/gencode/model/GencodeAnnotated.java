package org.monarchinitiative.sgenes.gencode.model;

import java.util.Set;

public interface GencodeAnnotated {

    GencodeMetadata gencodeMetadata();

    default Biotype biotype() {
        return gencodeMetadata().biotype();
    }

    default EvidenceLevel evidenceLevel() {
        return gencodeMetadata().evidenceLevel();
    }

    default Set<String> tags() {
        return gencodeMetadata().tags();
    }
}