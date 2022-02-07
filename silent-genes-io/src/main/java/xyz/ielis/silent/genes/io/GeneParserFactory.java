package xyz.ielis.silent.genes.io;

import org.monarchinitiative.svart.assembly.GenomicAssembly;
import xyz.ielis.silent.genes.io.json.JsonGeneParser;

public class GeneParserFactory {

    private final JsonGeneParser jsonGeneParser;

    private GeneParserFactory(GenomicAssembly assembly) {
        jsonGeneParser = JsonGeneParser.of(assembly); // only one of its kind is enough

    }

    public static GeneParserFactory of(GenomicAssembly assembly) {
        return new GeneParserFactory(assembly);
    }

    public GeneParser forFormat(SerializationFormat format) {
        switch (format) {
            case JSON:
                return jsonGeneParser;
            default:
                throw new IllegalArgumentException(String.format("Unknown serialization format: %s", format));
        }
    }

}
