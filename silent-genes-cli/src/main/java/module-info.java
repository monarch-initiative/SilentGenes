module xyz.ielis.silent.genes.cli {
    requires xyz.ielis.silent.genes.io;
    requires xyz.ielis.silent.genes.gencode;

    requires org.apache.commons.compress;
    requires info.picocli;
    requires org.slf4j;

    opens xyz.ielis.silent.genes.cli to info.picocli;
}