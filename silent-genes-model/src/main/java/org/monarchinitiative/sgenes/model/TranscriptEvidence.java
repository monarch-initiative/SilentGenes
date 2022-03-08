package org.monarchinitiative.sgenes.model;

/**
 * Levels of transcript evidence as provided by the transcript definition sources (e.g. <em>Gencode</em>, <em>RefSeq</em>).
 */
public enum TranscriptEvidence {

    // ******************************************* RefSeq Evidence ************************************************** //

    /**
     * RNA and protein products that are mainly derived from GenBank cDNA and EST data and are supported by the
     * RefSeq eukaryotic curation group. These records use accession prefixes <code>NM_</code>, <code>NR_</code>,
     * and <code>NP_</code>.
     */
    KNOWN(FeatureSource.REFSEQ),

    /**
     * RNA and protein products that are generated by the eukaryotic genome annotation pipeline. These records use accession prefixes XM_, XR_, and XP_
     */
    MODEL(FeatureSource.REFSEQ),

    // ******************************************* Gencode Evidence ************************************************* //

    /**
     * Pseudogene loci that were jointly predicted by the Yale Pseudopipe and UCSC Retrofinder pipelines as well
     * as by Havana manual annotation; other transcripts that were verified experimentally by RT-PCR and sequencing
     * through the <a href="https://europepmc.org/article/MED/22955982">GENCODE experimental pipeline</a>.
     */
    VALIDATED(FeatureSource.GENCODE),

    /**
     * Havana manual annotation (and Ensembl annotation where it is identical to Havana).
     */
    MANUAL_ANNOTATION(FeatureSource.GENCODE),

    /**
     * Ensembl loci where they are different from the Havana annotation or where no Havana annotation can be found.
     */
    AUTOMATED_ANNOTATION(FeatureSource.GENCODE);

    // ************************************************************************************************************** //

    private final FeatureSource source;

    TranscriptEvidence(FeatureSource source) {
        this.source = source;
    }

    public FeatureSource source() {
        return source;
    }

}