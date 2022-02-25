package org.monarchinitiative.sgenes.io.json;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.sgenes.io.GeneParserTestBase;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.sgenes.simple.Genes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JsonGeneParserTest extends GeneParserTestBase {

    @Test
    public void deserialize() throws Exception {
        JsonGeneParser parser = JsonGeneParser.of(ASSEMBLY);

        List<? extends Gene> actual = parser.read(JSON_TEST);

        List<Gene> expected = List.of(Genes.surf1(), Genes.surf2());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void roundTrip() throws Exception {
        JsonGeneParser parser = JsonGeneParser.of(ASSEMBLY);
        List<Gene> payload = List.of(Genes.surf1(), Genes.surf2());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        parser.write(payload, os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        List<Gene> deserialized = parser.read(is);

        assertThat(deserialized, equalTo(payload));
    }

    @Test
    @Disabled
    // Used to generate the payload to compare with in deserialize test.
    public void dump() throws Exception {
        List<Gene> genes = List.of(Genes.surf1(), Genes.surf2());
        JsonGeneParser parser = JsonGeneParser.of(ASSEMBLY);

        Path path = Paths.get("src/test/resources/xyz/ielis/silent/genes/io/json/test-surf1_surf2.json");
        try (OutputStream os = Files.newOutputStream(path)) {
            parser.write(genes, os);
        }
    }

}