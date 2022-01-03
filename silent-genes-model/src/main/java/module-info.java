module xyz.ielis.silent.genes.model {
    requires transitive org.monarchitiative.svart;

    exports xyz.ielis.silent.genes.model;
    exports xyz.ielis.silent.genes.model.impl to xyz.ielis.silent.genes.gencode, xyz.ielis.silent.genes.jannovar;
}