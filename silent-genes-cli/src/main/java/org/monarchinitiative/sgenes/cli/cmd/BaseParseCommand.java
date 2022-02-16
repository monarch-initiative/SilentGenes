package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.io.GeneParser;
import org.monarchinitiative.sgenes.io.GeneParserFactory;
import org.monarchinitiative.sgenes.io.SerializationFormat;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

abstract class BaseParseCommand implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseParseCommand.class);

    private static final NumberFormat NF = NumberFormat.getInstance(Locale.US);
    static {
        NF.setMaximumFractionDigits(2);
    }

    @CommandLine.Parameters(index = "0",
            description = "Path to input RefSeq GTF file.")
    protected Path gtfPath;

    @CommandLine.Parameters(index = "1",
            description = "Where to store the output.")
    private Path outputPath;


    @CommandLine.Option(names = {"-r", "--reference-genome"},
            description = "Genome assembly version (default: ${DEFAULT-VALUE}).",
            paramLabel = "{hg38, hg19}")
    private String assembly = "hg38";


    private static Optional<GenomicAssembly> parseAssembly(String assembly) {
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

    static int dumpGenesIntoJson(Path outputPath, GenomicAssembly genomicAssembly, List<? extends Gene> genes) {
        LOGGER.info("Serializing {} genes to `{}`", NF.format(genes.size()), outputPath.toAbsolutePath());
        GeneParserFactory parserFactory = GeneParserFactory.of(genomicAssembly);
        GeneParser jsonParser = parserFactory.forFormat(SerializationFormat.JSON);
        try (OutputStream os = openOutputForWriting(outputPath)) {
            jsonParser.write(genes, os);
        } catch (IOException e) {
            LOGGER.error("Error writing out genes: {}", e.getMessage(), e);
            return 1;
        }
        return 0;
    }

    private static OutputStream openOutputForWriting(Path outputPath) throws IOException {
        if (outputPath.toFile().getName().endsWith(".gz"))
            return new BufferedOutputStream(new GZIPOutputStream(Files.newOutputStream(outputPath)));
        else
            return Files.newOutputStream(outputPath);
    }

    protected abstract List<? extends Gene> readGenes(GenomicAssembly assembly);

    @Override
    public Integer call() {
        if (!Files.isRegularFile(gtfPath)) {
            LOGGER.error("The provided path does not point to a file: `{}`", gtfPath.toAbsolutePath());
        }

        Optional<GenomicAssembly> assemblyOptional = parseAssembly(assembly);
        if (assemblyOptional.isEmpty()) return 1;

        GenomicAssembly assembly = assemblyOptional.get();
        List<? extends Gene> genes = readGenes(assembly);

        return dumpGenesIntoJson(outputPath, assembly, genes);
    }
}
