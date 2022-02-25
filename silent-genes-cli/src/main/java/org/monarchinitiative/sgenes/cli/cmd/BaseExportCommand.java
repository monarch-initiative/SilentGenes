package org.monarchinitiative.sgenes.cli.cmd;

import org.monarchinitiative.sgenes.io.GeneParser;
import org.monarchinitiative.sgenes.io.GeneParserFactory;
import org.monarchinitiative.sgenes.io.SerializationFormat;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

abstract class BaseExportCommand extends BaseParseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseParseCommand.class);

    private static final NumberFormat NF = NumberFormat.getInstance(Locale.US);
    static {
        NF.setMaximumFractionDigits(2);
    }

    @Override
    public Integer call() {
        Optional<GenomicAssembly> assemblyOptional = parseAssembly(assembly);
        if (assemblyOptional.isEmpty()) return 1;

        GenomicAssembly assembly = assemblyOptional.get();
        List<? extends Gene> genes = readGenes(assembly);

        return dumpGenesIntoJson(outputPath, assembly, genes);
    }

    static int dumpGenesIntoJson(Path outputPath, GenomicAssembly genomicAssembly, List<? extends Gene> genes) {
        LOGGER.info("Serializing {} genes to `{}`", NF.format(genes.size()), outputPath.toAbsolutePath());
        GeneParserFactory parserFactory = GeneParserFactory.of(genomicAssembly);
        GeneParser jsonParser = parserFactory.forFormat(SerializationFormat.JSON);
        try {
            jsonParser.write(genes, outputPath);
        } catch (IOException e) {
            LOGGER.error("Error writing out genes: {}", e.getMessage(), e);
            return 1;
        }
        return 0;
    }


}
