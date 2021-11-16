module xyz.ielis.silent.genes.gencode {
    requires transitive xyz.ielis.silent.genes.model;
    requires org.apache.commons.compress; // despite being an automatic module, this is the latest version as of Nov 2021
    requires org.slf4j;

    exports xyz.ielis.silent.genes.gencode.model;
    exports xyz.ielis.silent.genes.gencode.io;
}