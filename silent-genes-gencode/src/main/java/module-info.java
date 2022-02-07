module org.monarchinitiative.sgenes.gencode {
    requires transitive org.monarchinitiative.sgenes.model;
    requires org.slf4j;

    exports org.monarchinitiative.sgenes.gencode.model;
    exports org.monarchinitiative.sgenes.gencode.io;
}