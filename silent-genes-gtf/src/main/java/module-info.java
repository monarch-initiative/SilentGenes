module org.monarchinitiative.sgenes.gtf {
    requires transitive org.monarchinitiative.sgenes.model;
    requires org.slf4j;

    exports org.monarchinitiative.sgenes.gtf.io;
    exports org.monarchinitiative.sgenes.gtf.model;
}