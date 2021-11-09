package xyz.ielis.silent.genes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.io.IOException;

public class TranscriptIdentifierSerializer extends StdSerializer<TranscriptIdentifier> {

    private static final long serialVersionUID = 1L;

    public TranscriptIdentifierSerializer() {
        this(TranscriptIdentifier.class);
    }

    protected TranscriptIdentifierSerializer(Class<TranscriptIdentifier> t) {
        super(t);
    }

    @Override
    public void serialize(TranscriptIdentifier id, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("acc", id.accession());
        gen.writeStringField("symbol", id.symbol());
        if (id.ccdsId().isPresent())
            gen.writeStringField("ccdsId", id.ccdsId().get());

        gen.writeEndObject();
    }
}
