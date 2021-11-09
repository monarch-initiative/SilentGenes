package xyz.ielis.silent.genes.model;

import xyz.ielis.silent.genes.model.impl.GeneIdentifierDefault;

import java.util.Optional;

public interface GeneIdentifier extends Identifier {

    static GeneIdentifier of(String accession,
                             String symbol,
                             String hgncId) {
        return new GeneIdentifierDefault(accession, symbol, hgncId);
    }

    // e.g. HGNC:11474
    Optional<String> hgncId();

}
