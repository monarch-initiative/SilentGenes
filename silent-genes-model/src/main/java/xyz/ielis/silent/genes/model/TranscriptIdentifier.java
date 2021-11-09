package xyz.ielis.silent.genes.model;

import xyz.ielis.silent.genes.model.impl.TranscriptIdentifierDefault;

import java.util.Optional;

public interface TranscriptIdentifier extends Identifier {

    static TranscriptIdentifier of(String accession, String symbol, String ccdsId) {
        return new TranscriptIdentifierDefault(accession, symbol, ccdsId);
    }

    Optional<String> ccdsId();

}
