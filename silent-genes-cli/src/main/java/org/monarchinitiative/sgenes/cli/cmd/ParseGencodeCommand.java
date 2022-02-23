package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.cli.Main;
import org.monarchinitiative.sgenes.gtf.io.GtfGeneParser;
import org.monarchinitiative.sgenes.gtf.io.GtfGeneParserFactory;
import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "parse-gencode",
        aliases = {"G"},
        header = "Parse Gencode GTF file.",
        mixinStandardHelpOptions = true,
        version = Main.VERSION,
        usageHelpWidth = Main.WIDTH,
        footer = Main.FOOTER)
public class ParseGencodeCommand extends BaseParseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseGencodeCommand.class);

    @Override
    protected List<? extends Gene> readGenes(GenomicAssembly assembly) {
        LOGGER.info("Reading GENCODE genes from `{}`", gtfPath.toAbsolutePath());
        GtfGeneParser<GencodeGene> parser = GtfGeneParserFactory.gencodeGeneParser(gtfPath, assembly);
        return parser.stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
