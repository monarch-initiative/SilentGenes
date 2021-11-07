package xyz.ielis.silent.genes.gencode.io;

import org.monarchinitiative.svart.GenomicAssembly;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;

import java.nio.file.Path;

public class GencodeParser implements Iterable<GencodeGene> {

    private final Path gencodeGtfPath;
    private final GenomicAssembly genomicAssembly;

    public GencodeParser(Path gencodeGtfPath, GenomicAssembly genomicAssembly) {
        this.gencodeGtfPath = gencodeGtfPath;
        this.genomicAssembly = genomicAssembly;
    }

    @Override
    public GeneIterator iterator() {
        return new GeneIterator(gencodeGtfPath, genomicAssembly);
    }

}
