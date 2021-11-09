package xyz.ielis.silent.genes.model.impl;

import xyz.ielis.silent.genes.model.GeneIdentifier;

import java.util.Objects;
import java.util.Optional;

public class GeneIdentifierDefault extends IdentifierDefault implements GeneIdentifier {

    private final String hgncId;

    public GeneIdentifierDefault(String accession,
                          String symbol,
                          String hgncId) {
        super(accession, symbol);
        this.hgncId = hgncId;
    }

    @Override
    public Optional<String> hgncId() {
        return Optional.ofNullable(hgncId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GeneIdentifierDefault that = (GeneIdentifierDefault) o;
        return Objects.equals(hgncId, that.hgncId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hgncId);
    }

    @Override
    public String toString() {
        return "GeneIdentifierDefault{" +
                "hgncId=" + hgncId +
                "} " + super.toString();
    }
}
