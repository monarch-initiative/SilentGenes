package xyz.ielis.silent.genes.cli;

import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import xyz.ielis.silent.genes.gencode.io.GencodeParser;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;
import xyz.ielis.silent.genes.io.GeneParser;
import xyz.ielis.silent.genes.io.GeneParserFactory;
import xyz.ielis.silent.genes.io.SerializationFormat;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

@CommandLine.Command(name = "parse-gencode",
        aliases = {"G"},
        header = "Parse GENCODE GTF file.",
        mixinStandardHelpOptions = true,
        version = Main.VERSION,
        usageHelpWidth = Main.WIDTH,
        footer = Main.FOOTER)
public class ParseGencodeCommand implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseGencodeCommand.class);

    @CommandLine.Parameters(index = "0",
            description = "Path to input Gencode GTF file.")
    public Path gencodeGtfPath;

    @CommandLine.Parameters(index = "1",
            description = "Where to store the output (default: ${DEFAULT-VALUE}).")
    public Path outputPath = Path.of("silent-genes.json.gz");

    @CommandLine.Option(names = {"-r", "--reference-genome"},
            description = "Genome assembly version (default: ${DEFAULT-VALUE}).",
            paramLabel = "{hg38, hg19}")
    public String assembly = "hg38";

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

    private static List<GencodeGene> readGenes(Path gencodeGtfPath, GenomicAssembly genomicAssembly) {
        LOGGER.info("Reading GENCODE genes from `{}`", gencodeGtfPath.toAbsolutePath());
        GencodeParser parser = new GencodeParser(gencodeGtfPath, genomicAssembly);
        return parser.stream().collect(Collectors.toUnmodifiableList());
    }

    private static int dumpGenesIntoJson(Path outputPath, GenomicAssembly genomicAssembly, List<GencodeGene> genes) {
        LOGGER.info("Serializing the genes to `{}`", outputPath.toAbsolutePath());
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

    @Override
    public Integer call() {
        // Checks Gencode input file
        if (!Files.isRegularFile(gencodeGtfPath)) {
            LOGGER.error("The provided path does not point to a file: `{}`", gencodeGtfPath.toAbsolutePath());
        }

        Optional<GenomicAssembly> genomicAssembly = parseAssembly(assembly);
        if (genomicAssembly.isEmpty()) return 1;


        List<GencodeGene> genes = readGenes(gencodeGtfPath, genomicAssembly.get());
        return dumpGenesIntoJson(outputPath, genomicAssembly.get(), genes);
    }
}
