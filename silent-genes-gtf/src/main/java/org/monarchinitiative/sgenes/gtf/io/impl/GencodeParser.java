package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.gtf.io.GtfGeneParser;
import org.monarchinitiative.svart.assembly.GenomicAssembly;

import java.nio.file.Path;
import java.util.Iterator;

public class GencodeParser implements GtfGeneParser<GencodeGene> {

    private final Path gencodeGtfPath;
    private final GenomicAssembly genomicAssembly;

    public GencodeParser(Path gencodeGtfPath, GenomicAssembly genomicAssembly) {
        this.gencodeGtfPath = gencodeGtfPath;
        this.genomicAssembly = genomicAssembly;
    }

    @Override
    public Iterator<GencodeGene> iterator() {
        return new GencodeGeneIterator(gencodeGtfPath, genomicAssembly);
    }

}
