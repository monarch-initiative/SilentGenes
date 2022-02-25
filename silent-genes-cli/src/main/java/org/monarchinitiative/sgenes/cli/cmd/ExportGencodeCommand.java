package org.monarchinitiative.sgenes.cli.cmd;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.monarchinitiative.sgenes.cli.Main;
import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscript;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

@CommandLine.Command(
        name = "export-gencode-metadata",
        aliases = {"GM"},
        header = "Export Gencode metadata",
        mixinStandardHelpOptions = true,
        version = Main.VERSION,
        usageHelpWidth = Main.WIDTH,
        footer = Main.FOOTER
)
public class ExportGencodeCommand extends BaseParseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportGencodeCommand.class);

    @Override
    protected List<? extends GencodeGene> readGenes(GenomicAssembly assembly) {
        return IOUtils.readGencodeGenes(assembly, validateInput());
    }

    @Override
    public Integer call() {
        Optional<GenomicAssembly> assemblyOptional = parseAssembly(assembly);
        if (assemblyOptional.isEmpty()) return 1;

        GenomicAssembly assembly = assemblyOptional.get();

        List<? extends GencodeGene> genes = readGenes(assembly);

        try {
            LOGGER.info("Writing metadata to `{}`", outputPath.toAbsolutePath());
            dumpGenes(genes, outputPath);
            LOGGER.info("Done!");
            return 0;
        } catch (IOException e) {
            LOGGER.error("Error writing out genes to {}: {}", outputPath.toAbsolutePath(), e.getMessage(), e);
            return 1;
        }
    }

    private static void dumpGenes(List<? extends GencodeGene> genes, Path output) throws IOException {
        try (BufferedWriter os = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(Files.newOutputStream(output))));
             CSVPrinter printer = CSVFormat.DEFAULT.print(os)) {
            // header
            printer.printRecord(List.of("gene_accession", "gene_symbol", "tx_accession", "tx_symbol", "base_biotype", "biotype", "evidence_level"));

            for (GencodeGene gene : genes) {
                for (Iterator<? extends GencodeTranscript> iterator = gene.transcripts(); iterator.hasNext(); ) {
                    GencodeTranscript tx = iterator.next();
                    printer.print(gene.accession());
                    printer.print(gene.symbol());
                    printer.print(tx.accession());
                    printer.print(tx.symbol());
                    printer.print(tx.biotype().getBaseType());
                    printer.print(tx.biotype());
                    printer.print(tx.evidenceLevel());
                    printer.println();
                }
            }
        }
    }

}
