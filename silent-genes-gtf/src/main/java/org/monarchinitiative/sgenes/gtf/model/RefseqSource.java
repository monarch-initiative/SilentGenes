package org.monarchinitiative.sgenes.gtf.model;

/**
 * RefSeq annotation source as described <a href="https://ftp.ncbi.nlm.nih.gov/genomes/README_GFF3.txt">here</a>.
 */
public enum RefseqSource {

    /**
     * Transcript projected from the alignment of a "known" RefSeq transcript to the genome.
     */
    BestRefSeq,

    /**
     * Transcript projected from the alignment of a curated RefSeq genomic sequence to the genome
     */
    CuratedGenomic,
    /**
     * Transcript based on the RefSeq database.
     */
    RefSeq,
    /**
     * Transcript predicted by <a href="https://www.ncbi.nlm.nih.gov/genome/annotation_euk/gnomon">Gnomon</a> procedure
     * using transcript and protein evidence and/or ab initio.
     */
    Gnomon,
    /**
     * Transcript predicted by tRNAscan-SE.
     */
    tRNAScanSe

}
