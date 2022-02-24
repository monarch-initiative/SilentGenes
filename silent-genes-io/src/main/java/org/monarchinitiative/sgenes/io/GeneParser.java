package org.monarchinitiative.sgenes.io;

import org.monarchinitiative.sgenes.model.Gene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public interface GeneParser {

    Logger LOGGER = LoggerFactory.getLogger(GeneParser.class);

    private static InputStream openForReading(Path source) throws IOException {
        if (source.toFile().getName().endsWith(".gz")) {
            LOGGER.debug("Assuming the file is gzipped: `{}`", source.toAbsolutePath());
            return new BufferedInputStream(new GZIPInputStream(Files.newInputStream(source)));
        } else {
            return Files.newInputStream(source);
        }
    }

    private static OutputStream openForWriting(Path destination) throws IOException {
        if (destination.toFile().getName().endsWith(".gz")) {
            LOGGER.debug("Assuming the file is gzipped: `{}`", destination.toAbsolutePath());
            return new BufferedOutputStream(new GZIPOutputStream(Files.newOutputStream(destination)));
        } else {
            return Files.newOutputStream(destination);
        }
    }

    void write(Iterable<? extends Gene> genes, OutputStream outputStream) throws IOException;

    /**
     * Persist a list of {@link Gene} into destination path.
     * <p>
     * The file will be compressed on-the-fly if the file name ends with <code>.gz</code>.
     *
     * @param genes       to store
     * @param destination where to persist the genes
     * @throws IOException if something truly terrible happens
     */
    default void write(Iterable<? extends Gene> genes, Path destination) throws IOException {
        try (OutputStream outputStream = openForWriting(destination)) {
            write(genes, outputStream);
        }
    }

    List<? extends Gene> read(InputStream inputStream) throws IOException;

    /**
     * Read {@link Gene}s from given <code>source</code>.
     * <p>
     * The file will be uncompressed on-the-fly if the file name ends with <code>.gz</code>.
     *
     * @param source path to the source file
     * @return list of genes
     * @throws IOException if something truly terrible happens
     */
    default List<? extends Gene> read(Path source) throws IOException {
        try (InputStream inputStream = openForReading(source)) {
            return read(inputStream);
        }
    }
}
