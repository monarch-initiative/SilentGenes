package org.monarchinitiative.sgenes.io.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.io.json.JsonGeneParser;

import java.io.IOException;

public class GenomicRegionSerializer extends StdSerializer<GenomicRegion> {

    private static final long serialVersionUID = 1L;

    public GenomicRegionSerializer() {
        this(GenomicRegion.class);
    }

    protected GenomicRegionSerializer(Class<GenomicRegion> t) {
        super(t);
    }

    @Override
    public void serialize(GenomicRegion region, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("gbAccession", region.contig().genBankAccession());
        gen.writeStringField("strand", region.strand().isPositive() ? "+" : "-");
        gen.writeNumberField("start", region.startWithCoordinateSystem(JsonGeneParser.CS));
        gen.writeNumberField("end", region.endWithCoordinateSystem(JsonGeneParser.CS));

        gen.writeEndObject();
    }
}
