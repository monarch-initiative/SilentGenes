package org.monarchinitiative.sgenes.jannovar;

import de.charite.compbio.jannovar.data.JannovarData;
import de.charite.compbio.jannovar.data.JannovarDataSerializer;
import de.charite.compbio.jannovar.data.SerializationException;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.monarchinitiative.sgenes.model.Gene;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JannovarParser implements Iterable<Gene> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JannovarParser.class);

    private final JannovarData jannovarData;
    private final GenomicAssembly assembly;

    public static JannovarParser of(Path jannovarCachePath, GenomicAssembly assembly) {
        JannovarData jannovarData = openJannovarCache(Objects.requireNonNull(jannovarCachePath, "Path to Jannovar cache must not be null"));
        return of(jannovarData, assembly);
    }

    public static JannovarParser of(JannovarData jannovarData, GenomicAssembly assembly) {
        return new JannovarParser(jannovarData, assembly);
    }

    private static JannovarData openJannovarCache(Path jannovarCachePath) {
        try {
            return new JannovarDataSerializer(jannovarCachePath.toAbsolutePath().toString()).load();
        } catch (SerializationException e) {
            LOGGER.warn("Error when reading cache at `{}`: {}", jannovarCachePath.toAbsolutePath(), e.getMessage(), e);
            return null;
        }
    }

    private JannovarParser(JannovarData jannovarData, GenomicAssembly assembly) {
        this.jannovarData = Objects.requireNonNull(jannovarData, "Jannovar data must not be null");
        this.assembly = Objects.requireNonNull(assembly, "Assembly must not be null");
    }

    @Override
    public Iterator<Gene> iterator() {
        return new JannovarIterator(jannovarData, assembly);
    }

    public Stream<Gene> stream() {
        Spliterator<Gene> spliterator = Spliterators.spliteratorUnknownSize(iterator(), Spliterator.IMMUTABLE);
        return StreamSupport.stream(spliterator, false);
    }
}
