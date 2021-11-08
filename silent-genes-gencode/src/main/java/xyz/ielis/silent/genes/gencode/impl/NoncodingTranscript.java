package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;

public class NoncodingTranscript extends BaseTranscript {

    private NoncodingTranscript(GenomicRegion location,
                                TranscriptIdentifier id,
                                String type,
                                EvidenceLevel evidenceLevel,
                                List<Coordinates> exons) {
        super(location, id, type, evidenceLevel, exons);
    }

    public static NoncodingTranscript of(GenomicRegion location,
                                         TranscriptIdentifier id,
                                         String type,
                                         EvidenceLevel evidenceLevel,
                                         List<Coordinates> exons) {
        Objects.requireNonNull(location, "Location must not be null");
        Objects.requireNonNull(id, "ID must not be null");
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        Objects.requireNonNull(exons, "Exons must not be null");
        if (exons.isEmpty()) {
            throw new IllegalArgumentException("Exon list must not be empty");
        }
        return new NoncodingTranscript(location, id, type, evidenceLevel, exons);
    }

    @Override
    public String toString() {
        return "NoncodingTranscript{} " + super.toString();
    }
}
