package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.cli.Main;
import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "parse-gencode",
        aliases = {"G"},
        header = "Parse Gencode GTF file.",
        mixinStandardHelpOptions = true,
        version = Main.VERSION,
        usageHelpWidth = Main.WIDTH,
        footer = Main.FOOTER)
public class ParseGencodeCommand extends BaseExportCommand {

    @Override
    protected List<? extends GencodeGene> readGenes(GenomicAssembly assembly) {
        return IOUtils.readGencodeGenes(assembly, validateInput());
    }

}
