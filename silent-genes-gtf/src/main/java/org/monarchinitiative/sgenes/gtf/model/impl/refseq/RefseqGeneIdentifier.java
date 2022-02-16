package org.monarchinitiative.sgenes.gtf.model.impl.refseq;

import org.monarchinitiative.sgenes.model.GeneIdentifier;

import java.util.Objects;
import java.util.Optional;

/**
 * RefSeq genes do not have a dedicated RefSeq gene accession, hence we use <em>NCBI gene ID</em> instead.
 */
public class RefseqGeneIdentifier implements GeneIdentifier {

    private final String ncbiGeneId;
    private final String symbol;
    private final String hgncGeneId;

    public static RefseqGeneIdentifier of(String ncbiGeneId,
                                          String symbol,
                                          String hgncGeneId) {
        return new RefseqGeneIdentifier(ncbiGeneId,
                symbol,
                hgncGeneId);
    }

    private RefseqGeneIdentifier(String ncbiGeneId, String symbol, String hgncGeneId) {
        this.ncbiGeneId = ncbiGeneId;
        this.symbol = symbol;
        this.hgncGeneId = hgncGeneId;
    }

    @Override
    public String accession() {
        return ncbiGeneId;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public Optional<String> hgncId() {
        return Optional.ofNullable(hgncGeneId);
    }

    @Override
    public Optional<String> ncbiGeneId() {
        return Optional.of(ncbiGeneId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefseqGeneIdentifier that = (RefseqGeneIdentifier) o;
        return Objects.equals(ncbiGeneId, that.ncbiGeneId) && Objects.equals(symbol, that.symbol) && Objects.equals(hgncGeneId, that.hgncGeneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ncbiGeneId, symbol, hgncGeneId);
    }

    @Override
    public String toString() {
        return "RefseqGeneIdentifier{" +
                "symbol='" + symbol + '\'' +
                ", ncbiGeneId='" + ncbiGeneId + '\'' +
                ", hgncGeneId='" + hgncGeneId + '\'' +
                '}';
    }
}
