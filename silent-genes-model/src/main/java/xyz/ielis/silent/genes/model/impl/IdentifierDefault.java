package xyz.ielis.silent.genes.model.impl;

import xyz.ielis.silent.genes.model.Identifier;

import java.util.Objects;

class IdentifierDefault implements Identifier {

    private final String accession;
    private final String symbol;

    IdentifierDefault(String accession, String symbol) {
        this.accession = Objects.requireNonNull(accession, "Accession must not be null");
        this.symbol = Objects.requireNonNull(symbol, "Symbol must not be null");
    }

    @Override
    public String accession() {
        return accession;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifierDefault that = (IdentifierDefault) o;
        return Objects.equals(accession, that.accession) && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accession, symbol);
    }

    @Override
    public String toString() {
        return "IdentifierDefault{" +
                "accession='" + accession + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
