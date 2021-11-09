package xyz.ielis.silent.genes.io.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicAssembly;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.io.GeneParser;
import xyz.ielis.silent.genes.io.json.deserialize.*;
import xyz.ielis.silent.genes.io.json.serialize.*;
import xyz.ielis.silent.genes.model.Gene;
import xyz.ielis.silent.genes.model.GeneIdentifier;
import xyz.ielis.silent.genes.model.Transcript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class JsonGeneParser implements GeneParser {

    public static final CoordinateSystem CS = CoordinateSystem.zeroBased();

    private static final Version VERSION = new Version(0, 1, 0, null, null, null);
    // A way to tell the Jackson library we read the JSON into a list of genes
    private static final TypeReference<List<Gene>> TYPE_REFERENCE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public static JsonGeneParser of(GenomicAssembly assembly) {
        return new JsonGeneParser(assembly);
    }

    private JsonGeneParser(GenomicAssembly assembly) {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(prepareModule(assembly));
    }

    private static SimpleModule prepareModule(GenomicAssembly assembly) {
        SimpleModule module = new SimpleModule("JsonGeneSerializer", VERSION);

        // serializers
        module.addSerializer(new CoordinateSerializer());
        module.addSerializer(new GenomicRegionSerializer());
        module.addSerializer(new TranscriptIdentifierSerializer());
        module.addSerializer(new TranscriptSerializer());
        module.addSerializer(new GeneIdentifierSerializer());
        module.addSerializer(new GeneSerializer());

        // deserializers
        module.addDeserializer(Coordinates.class, new CoordinateDeserializer());
        module.addDeserializer(Gene.class, new GeneDeserializer());
        module.addDeserializer(GeneIdentifier.class, new GeneIdentifierDeserializer());
        module.addDeserializer(GenomicRegion.class, new GenomicRegionDeserializer(assembly));
        module.addDeserializer(Transcript.class, new TranscriptDeserializer());
        module.addDeserializer(TranscriptIdentifier.class, new TranscriptIdentifierDeserializer());

        return module;
    }

    public List<Gene> read(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, TYPE_REFERENCE);
    }

    public void write(List<Gene> genes, OutputStream outputStream) throws IOException {
        objectMapper.writeValue(outputStream, genes);
    }

}
