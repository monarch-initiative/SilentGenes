module org.monarchinitiative.sgenes.jannovar {
    requires transitive org.monarchinitiative.sgenes.model;
    requires transitive jannovar.core; // there is little we can do with the unfortunate export of the automatic module
    requires com.google.common;
    requires org.slf4j;

    exports org.monarchinitiative.sgenes.jannovar;
}