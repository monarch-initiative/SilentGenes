package xyz.ielis.silent.genes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.monarchinitiative.svart.Coordinates;
import xyz.ielis.silent.genes.model.Coding;
import xyz.ielis.silent.genes.model.Transcript;

import java.io.IOException;

public class TranscriptSerializer extends StdSerializer<Transcript> {

    private static final long serialVersionUID = 1L;

    public TranscriptSerializer() {
        this(Transcript.class);
    }

    protected TranscriptSerializer(Class<Transcript> t) {
        super(t);
    }

    @Override
    public void serialize(Transcript tx, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", tx.id());
        gen.writeObjectField("loc", tx.location());
        gen.writeArrayFieldStart("exons");
        for (Coordinates exon : tx.exons()) {
            gen.writeObject(exon);
        }
        gen.writeEndArray();
        if (tx instanceof Coding) {
            Coding coding = (Coding) tx;
            gen.writeObjectField("startCodon", coding.startCodon());
            gen.writeObjectField("stopCodon", coding.stopCodon());
        }

        gen.writeEndObject();
    }
}
