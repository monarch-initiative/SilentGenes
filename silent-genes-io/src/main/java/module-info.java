module org.monarchinitiative.sgenes.io {
    requires transitive org.monarchinitiative.sgenes.model;

    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    exports org.monarchinitiative.sgenes.io;
}