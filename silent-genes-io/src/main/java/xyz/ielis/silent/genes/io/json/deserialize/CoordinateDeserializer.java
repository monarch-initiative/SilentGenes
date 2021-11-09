package xyz.ielis.silent.genes.io.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.monarchinitiative.svart.Coordinates;
import xyz.ielis.silent.genes.io.json.JsonGeneParser;

import java.io.IOException;

public class CoordinateDeserializer extends StdDeserializer<Coordinates> {

    public CoordinateDeserializer() {
        this(Coordinates.class);
    }

    protected CoordinateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Coordinates deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        int start = node.get("start").asInt();
        int end = node.get("end").asInt();

        return Coordinates.of(JsonGeneParser.CS, start, end);
    }

}
