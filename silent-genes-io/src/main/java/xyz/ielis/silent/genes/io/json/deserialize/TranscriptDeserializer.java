package xyz.ielis.silent.genes.io.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.model.Transcript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TranscriptDeserializer extends StdDeserializer<Transcript> {

    private static final long serialVersionUID = 1L;

    public TranscriptDeserializer() {
        this(Transcript.class);
    }

    protected TranscriptDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Transcript deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        TranscriptIdentifier id = codec.treeToValue(node.get("id"), TranscriptIdentifier.class);
        GenomicRegion location = codec.treeToValue(node.get("loc"), GenomicRegion.class);

        Iterator<JsonNode> exonIterator = node.get("exons").elements();
        List<Coordinates> exons = new LinkedList<>();
        while (exonIterator.hasNext()) {
            exons.add(codec.treeToValue(exonIterator.next(), Coordinates.class));
        }

        if (node.hasNonNull("startCodon") && node.hasNonNull("stopCodon")) {
            // coding transcript
            Coordinates startCodon = codec.treeToValue(node.get("startCodon"), Coordinates.class);
            Coordinates stopCodon = codec.treeToValue(node.get("stopCodon"), Coordinates.class);
            Coordinates fivePrimeRegion = codec.treeToValue(node.get("fivePrimeRegion"), Coordinates.class);
            Coordinates threePrimeRegion = codec.treeToValue(node.get("threePrimeRegion"), Coordinates.class);
            return Transcript.coding(id, location, exons, startCodon, stopCodon, fivePrimeRegion, threePrimeRegion);
        }

        // non-coding transcript
        return Transcript.noncoding(id, location, exons);
    }

}
