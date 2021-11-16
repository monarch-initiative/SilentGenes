package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.model.Coding;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CodingTranscript extends BaseTranscript implements Coding {

    private final Coordinates startCodon;
    private final Coordinates stopCodon;

    private CodingTranscript(GenomicRegion location,
                             TranscriptIdentifier id,
                             Coordinates startCodon,
                             Coordinates stopCodon,
                             Biotype biotype,
                             EvidenceLevel evidenceLevel,
                             List<Coordinates> exons,
                             Set<String> tags) {
        super(location, id, biotype, evidenceLevel, exons, tags);
        this.startCodon = startCodon;
        this.stopCodon = stopCodon;
    }

    public static CodingTranscript of(GenomicRegion location,
                                      TranscriptIdentifier id,
                                      Coordinates startCodon,
                                      Coordinates stopCodon,
                                      Biotype biotype,
                                      EvidenceLevel evidenceLevel,
                                      List<Coordinates> exons,
                                      Set<String> tags) {
        Objects.requireNonNull(location, "Location must not be null");
        Objects.requireNonNull(startCodon, "Start codon must not be null");
        Objects.requireNonNull(stopCodon, "Stop codon must not be null");
        Objects.requireNonNull(id, "ID must not be null");
        Objects.requireNonNull(biotype, "Biotype must not be null");
        Objects.requireNonNull(evidenceLevel, "Evidence level must not be null");
        Objects.requireNonNull(exons, "Exons must not be null");
        if (exons.isEmpty()) {
            throw new IllegalArgumentException("Exon list must not be empty");
        }
        Objects.requireNonNull(tags, "Tags must not be null");

        return new CodingTranscript(location, id, startCodon, stopCodon, biotype, evidenceLevel, exons, tags);
    }

    @Override
    public Coordinates startCodon() {
        return startCodon;
    }

    @Override
    public Coordinates stopCodon() {
        return stopCodon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CodingTranscript that = (CodingTranscript) o;
        return Objects.equals(startCodon, that.startCodon) && Objects.equals(stopCodon, that.stopCodon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startCodon, stopCodon);
    }

    @Override
    public String toString() {
        return "CodingTranscript{" +
                "startCodon=" + startCodon +
                ", stopCodon=" + stopCodon +
                "} " + super.toString();
    }
}
