package org.monarchinitiative.sgenes.model;

/**
 * Gene and transcript definition sources.
 */
public enum FeatureSource {
    /**
     * <a href="https://www.gencodegenes.org/pages/gencode.html">Gencode consortium</a>.
     */
    GENCODE,
    /**
     * <a href="https://www.ncbi.nlm.nih.gov/refseq/">RefSeq project</a>.
     */
    REFSEQ,
    /**
     * <a href="https://hgdownload.soe.ucsc.edu/goldenPath">UC Santa Cruz transcripts</a>
     */
    UCSC
}
