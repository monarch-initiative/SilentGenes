package org.monarchinitiative.sgenes.io.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.monarchinitiative.sgenes.io.GeneParser;
import org.monarchinitiative.sgenes.io.json.deserialize.*;
import org.monarchinitiative.sgenes.io.json.serialize.*;
import org.monarchinitiative.sgenes.model.*;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.GenomicRegion;

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
        module.addSerializer(new TranscriptMetadataSerializer());
        module.addSerializer(new TranscriptSerializer());
        module.addSerializer(new GeneIdentifierSerializer());
        module.addSerializer(new GeneSerializer());

        // deserializers
        module.addDeserializer(Coordinates.class, new CoordinateDeserializer());
        module.addDeserializer(GenomicRegion.class, new GenomicRegionDeserializer(assembly));
        module.addDeserializer(TranscriptIdentifier.class, new TranscriptIdentifierDeserializer());
        module.addDeserializer(TranscriptMetadata.class, new TranscriptMetadataDeserializer());
        module.addDeserializer(Transcript.class, new TranscriptDeserializer());
        module.addDeserializer(GeneIdentifier.class, new GeneIdentifierDeserializer());
        module.addDeserializer(Gene.class, new GeneDeserializer());

        return module;
    }

    public List<Gene> read(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, TYPE_REFERENCE);
    }

    public void write(Iterable<? extends Gene> genes, OutputStream outputStream) throws IOException {
        objectMapper.writeValue(outputStream, genes);
    }

}
