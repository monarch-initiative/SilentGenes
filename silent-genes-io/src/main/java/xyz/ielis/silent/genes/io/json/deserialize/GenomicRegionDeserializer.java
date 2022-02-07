package xyz.ielis.silent.genes.io.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.monarchinitiative.svart.Contig;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.svart.Strand;
import xyz.ielis.silent.genes.io.json.JsonGeneParser;

import java.io.IOException;

public class GenomicRegionDeserializer extends StdDeserializer<GenomicRegion> {

    private static final long serialVersionUID = 1L;

    private final GenomicAssembly assembly;

    public GenomicRegionDeserializer(GenomicAssembly assembly) {
        this(GenomicRegion.class, assembly);
    }

    protected GenomicRegionDeserializer(Class<?> vc, GenomicAssembly assembly) {
        super(vc);
        this.assembly = assembly;
    }

    @Override
    public GenomicRegion deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        String accession = node.get("gbAccession").asText();
        Contig contig = assembly.contigByName(accession);
        if (contig.isUnknown())
            throw new IllegalArgumentException("Unknown contig `" + accession + "`");

        Strand strand;
        switch (node.get("strand").asText()) {
            case "+":
                strand = Strand.POSITIVE;
                break;
            case "-":
                strand = Strand.NEGATIVE;
                break;
            default:
                throw new IllegalArgumentException("Unknown strand `" + node.get("strand").asText() + '`');
        }

        int start = node.get("start").asInt();
        int end = node.get("end").asInt();


        return GenomicRegion.of(contig, strand, JsonGeneParser.CS, start, end);
    }

}
