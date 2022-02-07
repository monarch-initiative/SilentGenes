package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.sgenes.model.impl.GeneIdentifierDefault;

import java.util.Optional;

public interface GeneIdentifier extends Identifier {

    static GeneIdentifier of(String accession,
                             String symbol,
                             String hgncId,
                             String ncbiGeneId) {
        return new GeneIdentifierDefault(accession, symbol, hgncId, ncbiGeneId);
    }

    /**
     * @return <em>HGNC</em> id of the gene, if available (e.g. <code>HGNC:11474</code>)
     */
    Optional<String> hgncId();

    /**
     * @return <em>NCBIGene</em> id of the gene, if available (e.g. <code>NCBIGene:6834</code>)
     */
    Optional<String> ncbiGeneId();
}
