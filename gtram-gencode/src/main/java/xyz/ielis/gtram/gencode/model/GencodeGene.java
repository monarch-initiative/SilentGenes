package xyz.ielis.gtram.gencode.model;

import xyz.ielis.gtram.model.Gene;

public interface GencodeGene extends Gene {

    // e.g. ENSG00000278267.1
    String id();

    // e.g. protein_coding, miRNA, lncRNA, etc.
    String type();

    Status status();

    // e.g. FBN1
    String name();

    EvidenceLevel evidenceLevel();

}
