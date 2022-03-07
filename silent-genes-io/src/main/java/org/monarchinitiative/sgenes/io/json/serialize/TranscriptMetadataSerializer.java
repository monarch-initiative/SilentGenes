package org.monarchinitiative.sgenes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.monarchinitiative.sgenes.model.TranscriptMetadata;

import java.io.IOException;

public class TranscriptMetadataSerializer extends StdSerializer<TranscriptMetadata> {

    private static final long serialVersionUID = 1L;

    public TranscriptMetadataSerializer() {
        this(TranscriptMetadata.class);
    }

    protected TranscriptMetadataSerializer(Class<TranscriptMetadata> t) {
        super(t);
    }

    @Override
    public void serialize(TranscriptMetadata value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        if (value.evidence().isPresent())
            gen.writeStringField("evidence", value.evidence().get().name());

        gen.writeEndObject();
    }
}
