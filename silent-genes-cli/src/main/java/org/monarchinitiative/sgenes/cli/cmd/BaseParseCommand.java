package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Callable;

abstract class BaseParseCommand implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseParseCommand.class);

    private static final NumberFormat NF = NumberFormat.getInstance(Locale.US);
    static {
        NF.setMaximumFractionDigits(2);
    }

    @CommandLine.Option(names = {"-r", "--reference-genome"},
            description = "Genome assembly version (default: ${DEFAULT-VALUE}).",
            paramLabel = "{hg38, hg19}")
    protected String assembly = "hg38";

    @CommandLine.Option(names = {"-i", "--input"},
            description = "Path to input RefSeq GTF file.")
    private Path gtfPath;

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Where to store the output.")
    protected Path outputPath;

    protected Path validateInput() {
        if (!Files.isRegularFile(gtfPath)) {
            LOGGER.error("The provided path does not point to a file: `{}`", gtfPath.toAbsolutePath());
            throw new IllegalArgumentException("The provided path does not point to a file: `" + gtfPath.toAbsolutePath() +"`");
        }
        return gtfPath;
    }

    protected static Optional<GenomicAssembly> parseAssembly(String assembly) {
        switch (assembly.toLowerCase()) {
            case "hg19":
            case "grch37":
                LOGGER.info("Using GRCh37.p13 as genomic assembly");
                return Optional.of(GenomicAssemblies.GRCh37p13());
            case "hg38":
            case "grch38":
                LOGGER.info("Using GRCh38.p13 as genomic assembly");
                return Optional.of(GenomicAssemblies.GRCh38p13());
            default:
                LOGGER.error("Unknown genomic assembly value `{}`", assembly);
                return Optional.empty();
        }
    }

    protected abstract List<? extends Gene> readGenes(GenomicAssembly assembly);

}
