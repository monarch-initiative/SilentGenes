package org.monarchinitiative.sgenes.io.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.monarchinitiative.sgenes.model.TranscriptEvidence;
import org.monarchinitiative.sgenes.model.TranscriptMetadata;

import java.io.IOException;

public class TranscriptMetadataDeserializer extends StdDeserializer<TranscriptMetadata> {

    private static final long serialVersionUID = 1L;

    public TranscriptMetadataDeserializer() {
        this(TranscriptMetadata.class);
    }

    protected TranscriptMetadataDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TranscriptMetadata deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        TranscriptEvidence evidence = null;
        if (node.has("evidence"))
            evidence = TranscriptEvidence.valueOf(node.get("evidence").asText());

        return TranscriptMetadata.of(evidence);
    }

}
