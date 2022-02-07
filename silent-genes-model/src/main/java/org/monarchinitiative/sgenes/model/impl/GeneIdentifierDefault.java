package org.monarchinitiative.sgenes.model.impl;

import org.monarchinitiative.sgenes.model.GeneIdentifier;

import java.util.Objects;
import java.util.Optional;

public class GeneIdentifierDefault extends IdentifierDefault implements GeneIdentifier {

    private final String hgncId;
    private final String ncbiGeneId;

    public GeneIdentifierDefault(String accession,
                                 String symbol,
                                 String hgncId,
                                 String ncbiGeneId) {
        super(accession, symbol);
        this.hgncId = hgncId;
        this.ncbiGeneId = ncbiGeneId;
    }

    @Override
    public Optional<String> hgncId() {
        return Optional.ofNullable(hgncId);
    }

    @Override
    public Optional<String> ncbiGeneId() {
        return Optional.ofNullable(ncbiGeneId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GeneIdentifierDefault that = (GeneIdentifierDefault) o;
        return Objects.equals(hgncId, that.hgncId) && Objects.equals(ncbiGeneId, that.ncbiGeneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hgncId, ncbiGeneId);
    }

    @Override
    public String toString() {
        return "GeneIdentifierDefault{" +
                "hgncId='" + hgncId + '\'' +
                ", ncbiGeneId='" + ncbiGeneId + '\'' +
                "} " + super.toString();
    }
}
