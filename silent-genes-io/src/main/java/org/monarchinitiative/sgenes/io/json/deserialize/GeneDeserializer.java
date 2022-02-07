package org.monarchinitiative.sgenes.io.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.Transcript;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GeneDeserializer extends StdDeserializer<Gene> {

    private static final long serialVersionUID = 1L;

    public GeneDeserializer() {
        this(Gene.class);
    }

    protected GeneDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Gene deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        GeneIdentifier id = codec.treeToValue(node.get("id"), GeneIdentifier.class);
        GenomicRegion location = codec.treeToValue(node.get("loc"), GenomicRegion.class);

        Iterator<JsonNode> transcriptIterator = node.get("transcripts").elements();
        List<Transcript> transcripts = new LinkedList<>();
        while (transcriptIterator.hasNext()) {
            transcripts.add(codec.treeToValue(transcriptIterator.next(), Transcript.class));
        }

        return Gene.of(id, location, transcripts);
    }
}
