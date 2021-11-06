module xyz.ielis.gtram.gencode {
    requires transitive xyz.ielis.gtram.model;
    requires org.slf4j;

    exports xyz.ielis.gtram.gencode.model;
    exports xyz.ielis.gtram.gencode.io;
}