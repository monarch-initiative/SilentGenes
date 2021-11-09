package xyz.ielis.silent.genes.io.json;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.*;
import xyz.ielis.silent.genes.model.Gene;
import xyz.ielis.silent.genes.simple.Genes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JsonGeneParserTest {

    private static final GenomicAssembly ASSEMBLY = GenomicAssemblies.GRCh38p13();

    @Test
    public void deserialize() throws Exception {
        JsonGeneParser parser = JsonGeneParser.of(ASSEMBLY);

        List<Gene> actual = new ArrayList<>();
        try (InputStream is = JsonGeneParserTest.class.getResourceAsStream("test-surf1_surf2.json")) {
            actual.addAll(parser.read(is));

        }
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

    //@Test
    // Used to generate the paylod to compare with in deserialize test.
    public void dump() throws Exception {
        List<Gene> genes = List.of(Genes.surf1(), Genes.surf2());
        JsonGeneParser parser = JsonGeneParser.of(ASSEMBLY);

        Path path = Paths.get("src/test/resources/xyz/ielis/silent/genes/io/json/test-surf1_surf2.json");
        try (OutputStream os = Files.newOutputStream(path)) {
            parser.write(genes, os);
        }
    }

}