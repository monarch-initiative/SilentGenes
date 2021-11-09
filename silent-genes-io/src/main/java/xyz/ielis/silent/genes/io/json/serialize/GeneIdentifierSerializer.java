package xyz.ielis.silent.genes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.ielis.silent.genes.model.GeneIdentifier;

import java.io.IOException;

public class GeneIdentifierSerializer extends StdSerializer<GeneIdentifier> {

    private static final long serialVersionUID = 1L;

    public GeneIdentifierSerializer() {
        this(GeneIdentifier.class);
    }

    protected GeneIdentifierSerializer(Class<GeneIdentifier> t) {
        super(t);
    }

    @Override
    public void serialize(GeneIdentifier id, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("acc", id.accession());
        gen.writeStringField("symbol", id.symbol());
        if (id.hgncId().isPresent())
            gen.writeStringField("hgncId", id.hgncId().get());

        gen.writeEndObject();
    }
}
