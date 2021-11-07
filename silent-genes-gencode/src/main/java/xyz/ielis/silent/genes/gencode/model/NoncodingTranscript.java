package xyz.ielis.silent.genes.gencode.model;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;

// TODO - does this have to be public?
public class NoncodingTranscript extends BaseTranscript {

    public static NoncodingTranscript of(GenomicRegion location,
                                         String id,
                                         String type,
                                         Status status,
                                         String name,
                                         EvidenceLevel evidenceLevel,
                                         List<Coordinates> exons) {
        // TODO - null checks
        return new NoncodingTranscript(location, id, type, status, name, evidenceLevel, exons);
    }

    private NoncodingTranscript(GenomicRegion location,
                                String id,
                                String type,
                                Status status,
                                String name,
                                EvidenceLevel evidenceLevel,
                                List<Coordinates> exons) {
        super(location, id, type, status, name, evidenceLevel, exons);
    }

    @Override
    public String toString() {
        return "NoncodingTranscript{} " + super.toString();
    }
}
