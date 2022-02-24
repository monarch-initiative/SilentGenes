package org.monarchinitiative.sgenes.io;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.sgenes.io.json.JsonGeneParser;
import org.monarchinitiative.sgenes.model.Gene;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static org.hamcrest.Matchers.hasSize;

public class GeneParserTest extends GeneParserTestBase {

    public static final JsonGeneParser GENE_PARSER = JsonGeneParser.of(ASSEMBLY);
    private static List<? extends Gene> GENES;

    private GeneParser parser;

    private static Path prepareCompressedJson() throws IOException {
        Path dest = Files.createTempFile("sg", ".json.gz");
        dest.toFile().deleteOnExit();
        try (OutputStream os = new BufferedOutputStream(new GZIPOutputStream(Files.newOutputStream(dest)))) {
            ((GeneParser) GENE_PARSER).write(GENES, os);
        }
        return dest;
    }

    @BeforeAll
    public static void beforeAll() throws Exception {
        GENES = GENE_PARSER.read(JSON_TEST);
    }

    @BeforeEach
    public void setUp() {
        GeneParserFactory parserFactory = GeneParserFactory.of(ASSEMBLY);
        parser = parserFactory.forFormat(SerializationFormat.JSON);
    }

    @Test
    public void read() throws Exception {
        Path dest = prepareCompressedJson();

        List<? extends Gene> genes = parser.read(dest);

        MatcherAssert.assertThat(genes, hasSize(2));
    }
}