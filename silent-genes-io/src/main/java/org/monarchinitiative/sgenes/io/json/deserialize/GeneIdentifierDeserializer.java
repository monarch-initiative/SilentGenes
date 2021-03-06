package org.monarchinitiative.sgenes.io.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.monarchinitiative.sgenes.model.GeneIdentifier;

import java.io.IOException;

public class GeneIdentifierDeserializer extends StdDeserializer<GeneIdentifier> {

    private static final long serialVersionUID = 1L;

    public GeneIdentifierDeserializer() {
        this(GeneIdentifier.class);
    }

    protected GeneIdentifierDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public GeneIdentifier deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        String accession = node.get("acc").asText();
        String symbol = node.get("symbol").asText();

        String hgncId = null;
        if (node.has("hgncId")) {
            hgncId = node.get("hgncId").asText();
        }

        String ncbiGeneId = null;
        if (node.has("ncbiGeneId")) {
            ncbiGeneId = node.get("ncbiGeneId").asText();
        }

        return GeneIdentifier.of(accession, symbol, hgncId, ncbiGeneId);
    }
}
