package org.monarchinitiative.sgenes.io.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;

import java.io.IOException;

public class TranscriptIdentifierDeserializer extends StdDeserializer<TranscriptIdentifier> {

    private static final long serialVersionUID = 1L;

    public TranscriptIdentifierDeserializer() {
        this(TranscriptIdentifier.class);
    }

    protected TranscriptIdentifierDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TranscriptIdentifier deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        String accession = node.get("acc").asText();
        String symbol = node.get("symbol").asText();

        String ccdsId = null;
        if (node.has("ccdsId")) {
            ccdsId = node.get("ccdsId").asText();
        }

        return TranscriptIdentifier.of(accession, symbol, ccdsId);
    }
}
