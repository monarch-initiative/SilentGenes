package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NoncodingTranscript extends BaseTranscript {

    private NoncodingTranscript(GenomicRegion location,
                                TranscriptIdentifier id,
                                Biotype biotype,
                                EvidenceLevel evidenceLevel,
                                List<Coordinates> exons,
                                Set<String> tags) {
        super(location, id, biotype, evidenceLevel, exons, tags);
    }

    public static NoncodingTranscript of(GenomicRegion location,
                                         TranscriptIdentifier id,
                                         Biotype biotype,
                                         EvidenceLevel evidenceLevel,
                                         List<Coordinates> exons,
                                         Set<String> tags) {
        Objects.requireNonNull(location, "Location must not be null");
        Objects.requireNonNull(id, "ID must not be null");
        Objects.requireNonNull(biotype, "Biotype must not be null");
        Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        Objects.requireNonNull(exons, "Exons must not be null");
        if (exons.isEmpty()) {
            throw new IllegalArgumentException("Exon list must not be empty");
        }
        Objects.requireNonNull(tags, "Tags must not be null");

        return new NoncodingTranscript(location, id, biotype, evidenceLevel, exons, tags);
    }

    @Override
    public String toString() {
        return "NoncodingTranscript{} " + super.toString();
    }
}
