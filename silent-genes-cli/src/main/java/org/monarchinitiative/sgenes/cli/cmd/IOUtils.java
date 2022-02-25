package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.gtf.io.GtfGeneParser;
import org.monarchinitiative.sgenes.gtf.io.GtfGeneParserFactory;
import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    private IOUtils() {
    }

    static List<? extends GencodeGene> readGencodeGenes(GenomicAssembly assembly, Path path) {
        LOGGER.info("Reading Gencode genes from `{}`", path.toAbsolutePath());
        GtfGeneParser<GencodeGene> parser = GtfGeneParserFactory.gencodeGeneParser(path, assembly);
        return parser.stream()
                .collect(Collectors.toUnmodifiableList());
    }

    static List<? extends RefseqGene> readRefseqGenes(GenomicAssembly assembly, Path path) {
        LOGGER.info("Reading RefSeq genes from `{}`", path.toAbsolutePath());
        GtfGeneParser<RefseqGene> parser = GtfGeneParserFactory.refseqGtfParser(path, assembly);
        return parser.stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
