package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.cli.Main;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "parse-refseq",
        aliases = {"R"},
        header = "Parse RefSeq GTF file.",
        mixinStandardHelpOptions = true,
        version = Main.VERSION,
        usageHelpWidth = Main.WIDTH,
        footer = Main.FOOTER)
public class ParseRefseqCommand extends BaseExportCommand {

    @Override
    protected List<? extends RefseqGene> readGenes(GenomicAssembly assembly) {
        return IOUtils.readRefseqGenes(assembly, validateInput());
    }

}
