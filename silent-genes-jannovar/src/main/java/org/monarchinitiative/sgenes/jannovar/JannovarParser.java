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

    private static final List<Gene> EMPTY_GENE_LIST = List.of();

    private final JannovarData jannovarData;
    private final GenomicAssembly assembly;

    private JannovarParser(Path jannovarCachePath, GenomicAssembly assembly) {
        this.jannovarData = openJannovarCache(Objects.requireNonNull(jannovarCachePath, "Path to Jannovar cache must not be null"));
        this.assembly = Objects.requireNonNull(assembly, "Assembly must not be null");
    }

    public static JannovarParser of(Path jannovarCachePath, GenomicAssembly assembly) {
        return new JannovarParser(jannovarCachePath, assembly);
    }

    private static JannovarData openJannovarCache(Path jannovarCachePath) {
        try {
            return new JannovarDataSerializer(jannovarCachePath.toAbsolutePath().toString()).load();
        } catch (SerializationException e) {
            LOGGER.warn("Error when reading cache at `{}`: {}", jannovarCachePath.toAbsolutePath(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Iterator<Gene> iterator() {
        if (jannovarData == null)
            return EMPTY_GENE_LIST.iterator();
        else
            return new JannovarIterator(jannovarData, assembly);
    }

    public Stream<Gene> stream() {
        Spliterator<Gene> spliterator = Spliterators.spliteratorUnknownSize(iterator(), Spliterator.IMMUTABLE);
        return StreamSupport.stream(spliterator, false);
    }
}
