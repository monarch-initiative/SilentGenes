package org.monarchinitiative.sgenes.gtf.io;

import org.monarchinitiative.sgenes.gtf.io.impl.RefseqParser;
import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.gtf.io.impl.GencodeParser;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;

import java.nio.file.Path;

public class GtfGeneParserFactory {

    public static GtfGeneParser<GencodeGene> gtfGeneParser(Path gencodeGtfPath, GenomicAssembly genomicAssembly) {
        return new GencodeParser(gencodeGtfPath, genomicAssembly);
    }

    public static GtfGeneParser<RefseqGene> refseqGtfParser(Path refseqGtfPath, GenomicAssembly genomicAssembly) {
        return new RefseqParser(refseqGtfPath, genomicAssembly);
    }

}
