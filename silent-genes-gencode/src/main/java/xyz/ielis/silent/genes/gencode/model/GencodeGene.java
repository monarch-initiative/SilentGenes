package xyz.ielis.silent.genes.gencode.model;

import xyz.ielis.silent.genes.model.Gene;

public interface GencodeGene extends Gene {

    // e.g. protein_coding, miRNA, lncRNA, etc.
    // TODO - implement a fat enum based on `https://www.gencodegenes.org/pages/biotypes.html`
    String type();

    EvidenceLevel evidenceLevel();

    @Override
    Iterable<? extends GencodeTranscript> transcripts();
}
