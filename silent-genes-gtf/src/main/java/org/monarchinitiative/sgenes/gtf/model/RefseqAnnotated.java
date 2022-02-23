package org.monarchinitiative.sgenes.gtf.model;

public interface RefseqAnnotated {

    RefseqMetadata refseqMetadata();

    default Biotype biotype() {
        return refseqMetadata().biotype();
    }

}
