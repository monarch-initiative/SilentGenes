package xyz.ielis.silent.genes.model;

public interface Identifier {

    // e.g. ENSG00000148290.10, ENST00000371974.8
    String accession();

    // e.g. SURF1
    String symbol();

}
