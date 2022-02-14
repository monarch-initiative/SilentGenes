package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.cli.Main;
import org.monarchinitiative.sgenes.gtf.io.GtfGeneParser;
import org.monarchinitiative.sgenes.gtf.io.GtfGeneParserFactory;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "parse-refseq",
        aliases = {"R"},
        header = "Parse RefSeq GTF file.",
        mixinStandardHelpOptions = true,
        version = Main.VERSION,
        usageHelpWidth = Main.WIDTH,
        footer = Main.FOOTER)
public class ParseRefseqCommand extends BaseParseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseRefseqCommand.class);

    @Override
    protected List<? extends Gene> readGenes(GenomicAssembly assembly) {
        LOGGER.info("Reading RefSeq genes from `{}`", gtfPath.toAbsolutePath());
        GtfGeneParser<RefseqGene> parser = GtfGeneParserFactory.refseqGtfParser(gtfPath, assembly);
        return parser.stream()
                .collect(Collectors.toUnmodifiableList());
    }

}
