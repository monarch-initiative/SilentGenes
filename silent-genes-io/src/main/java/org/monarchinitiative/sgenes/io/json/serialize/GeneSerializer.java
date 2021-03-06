package org.monarchinitiative.sgenes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.sgenes.model.Identified;
import org.monarchinitiative.sgenes.model.Transcript;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        // sort before writing out to get deterministic output
        List<Transcript> transcripts = gene.transcriptStream()
                .sorted(Comparator.comparing(Identified::accession))
                .collect(Collectors.toUnmodifiableList());

        gen.writeArrayFieldStart("transcripts");
        for (Transcript tx : transcripts) {
            gen.writeObject(tx);
        }

        gen.writeEndArray();

        gen.writeEndObject();
    }
}
