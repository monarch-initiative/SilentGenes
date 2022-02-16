package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.io.GtfGeneParser;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;

import java.nio.file.Path;
import java.util.Iterator;

public class RefseqParser implements GtfGeneParser<RefseqGene> {

    private final Path refseqGtfPath;
    private final GenomicAssembly genomicAssembly;

    public RefseqParser(Path refseqGtfPath, GenomicAssembly genomicAssembly) {
        this.refseqGtfPath = refseqGtfPath;
        this.genomicAssembly = genomicAssembly;
    }

    @Override
    public Iterator<RefseqGene> iterator() {
        return new RefseqGeneIterator(refseqGtfPath, genomicAssembly);
    }

}
