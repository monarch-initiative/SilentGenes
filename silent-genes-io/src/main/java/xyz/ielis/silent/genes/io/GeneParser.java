package xyz.ielis.silent.genes.io;

import xyz.ielis.silent.genes.model.Gene;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface GeneParser {

    void write(List<? extends Gene> genes, OutputStream outputStream) throws IOException;

    default void write(List<? extends Gene> genes, Path destination) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(destination)) {
            write(genes, outputStream);
        }
    }

    List<? extends Gene> read(InputStream inputStream) throws IOException;

    default List<? extends Gene> read(Path source) throws IOException {
        try (InputStream inputStream = Files.newInputStream(source)) {
            return read(inputStream);
        }
    }

}
