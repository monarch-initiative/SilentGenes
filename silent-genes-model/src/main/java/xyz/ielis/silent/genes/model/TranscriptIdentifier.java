package xyz.ielis.silent.genes.model;

import java.util.Optional;

public interface TranscriptIdentifier extends Identifier {

    static TranscriptIdentifier of(String accession, String symbol, String ccdsId) {
        return new TranscriptIdentifierDefault(accession, symbol, ccdsId);
    }

    Optional<String> ccdsId();

}
