package xyz.ielis.silent.genes.model.impl;

import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.Objects;
import java.util.Optional;

public class TranscriptIdentifierDefault extends IdentifierDefault implements TranscriptIdentifier {

    private final String ccdsId;

    public TranscriptIdentifierDefault(String accession, String symbol, String ccdsId) {
        super(accession, symbol);
        this.ccdsId = ccdsId;
    }

    @Override
    public Optional<String> ccdsId() {
        return Optional.ofNullable(ccdsId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TranscriptIdentifierDefault that = (TranscriptIdentifierDefault) o;
        return Objects.equals(ccdsId, that.ccdsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ccdsId);
    }

    @Override
    public String toString() {
        return "TranscriptIdentifierDefault{" +
                "ccdsId='" + ccdsId + '\'' +
                "} " + super.toString();
    }
}
