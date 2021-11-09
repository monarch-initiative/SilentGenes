package xyz.ielis.silent.genes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.ielis.silent.genes.model.Gene;
import xyz.ielis.silent.genes.model.Transcript;

import java.io.IOException;

public class GeneSerializer extends StdSerializer<Gene> {

    private static final long serialVersionUID = 1L;

    public GeneSerializer() {
        this(Gene.class);
    }

    protected GeneSerializer(Class<Gene> t) {
        super(t);
    }

    @Override
    public void serialize(Gene gene, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", gene.id());
        gen.writeObjectField("loc", gene.location());

        gen.writeArrayFieldStart("transcripts");
        for (Transcript tx : gene.transcripts()) {
            gen.writeObject(tx);
        }
        gen.writeEndArray();

        gen.writeEndObject();
    }
}
