package org.monarchinitiative.sgenes.cli;

import org.monarchinitiative.sgenes.cli.cmd.ExportGencodeCommand;
import org.monarchinitiative.sgenes.cli.cmd.ExportRefseqCommand;
import org.monarchinitiative.sgenes.cli.cmd.ParseGencodeCommand;
import org.monarchinitiative.sgenes.cli.cmd.ParseRefseqCommand;
import picocli.CommandLine;

import java.util.Locale;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Help.Ansi.Style.*;

@CommandLine.Command(name = "silent-genes-cli.jar",
        header = "A library that lets you silently work with genes and transcripts",
        mixinStandardHelpOptions = true,
        version = Main.VERSION,
        usageHelpWidth = Main.WIDTH,
        footer = Main.FOOTER
)
public class Main implements Callable<Integer> {
    public static final String VERSION = "silent-genes-cli v0.2.4";

    public static final int WIDTH = 120;

    public static final String FOOTER = "See the full documentation at https://github.com/ielis/SilentGenes";

    private static final CommandLine.Help.ColorScheme COLOR_SCHEME = new CommandLine.Help.ColorScheme.Builder()
            .commands(bold, fg_blue, underline)
            .options(fg_yellow)
            .parameters(fg_yellow)
            .optionParams(italic)
            .build();

    private static CommandLine commandLine;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        commandLine = new CommandLine(new Main())
                .setColorScheme(COLOR_SCHEME)
                .addSubcommand("parse-gencode", new ParseGencodeCommand())
                .addSubcommand("parse-refseq", new ParseRefseqCommand())
                .addSubcommand("export-gencode-metadata", new ExportGencodeCommand())
                .addSubcommand("export-refseq-metadata", new ExportRefseqCommand());
        commandLine.setToggleBooleanFlags(false);
        System.exit(commandLine.execute(args));

    }

    @Override
    public Integer call() {
        // work done in subcommands
        commandLine.usage(commandLine.getOut());
        return 0;
    }
}
