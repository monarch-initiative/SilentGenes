package xyz.ielis.silent.genes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.monarchinitiative.svart.Coordinates;
import xyz.ielis.silent.genes.io.json.JsonGeneParser;

import java.io.IOException;

public class CoordinateSerializer extends StdSerializer<Coordinates> {

    public CoordinateSerializer() {
        this(Coordinates.class);
    }

    protected CoordinateSerializer(Class<Coordinates> t) {
        super(t);
    }

    @Override
    public void serialize(Coordinates coordinates, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("start", coordinates.startWithCoordinateSystem(JsonGeneParser.CS));
        gen.writeNumberField("end", coordinates.endWithCoordinateSystem(JsonGeneParser.CS));

        gen.writeEndObject();
    }
}
