module org.monarchinitiative.sgenes.cli {
    requires org.monarchinitiative.sgenes.io;
    requires org.monarchinitiative.sgenes.gencode;

    requires info.picocli;
    requires org.slf4j;

    opens org.monarchinitiative.sgenes.cli to info.picocli;
}