package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.sgenes.model.impl.TranscriptIdentifierDefault;

import java.util.Optional;

public interface TranscriptIdentifier extends Identifier {

    static TranscriptIdentifier of(String accession, String symbol, String ccdsId) {
        return new TranscriptIdentifierDefault(accession, symbol, ccdsId);
    }

    Optional<String> ccdsId();

}
