module xyz.ielis.silent.genes.gencode {
    requires transitive xyz.ielis.silent.genes.model;
    requires org.apache.commons.compress;
    requires org.slf4j;

    exports xyz.ielis.silent.genes.gencode.model;
    exports xyz.ielis.silent.genes.gencode.io;
}