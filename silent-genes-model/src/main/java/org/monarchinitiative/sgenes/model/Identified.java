package org.monarchinitiative.sgenes.model;

public interface Identified<T extends Identifier> {

    T id();

    default String accession() {
        return id().accession();
    }

    default String symbol() {
        return id().symbol();
    }

}
