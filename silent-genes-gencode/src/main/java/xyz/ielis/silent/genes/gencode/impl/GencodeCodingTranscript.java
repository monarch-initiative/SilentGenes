package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.model.CodingTranscript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GencodeCodingTranscript extends GencodeBaseTranscript implements CodingTranscript {

    private final Coordinates cdsCoordinates;

    private GencodeCodingTranscript(GenomicRegion location,
                                    TranscriptIdentifier id,
                                    Coordinates cdsCoordinates,
                                    Biotype biotype,
                                    EvidenceLevel evidenceLevel,
                                    List<Coordinates> exons,
                                    Set<String> tags) {
        super(location, id, biotype, evidenceLevel, exons, tags);
        this.cdsCoordinates = Objects.requireNonNull(cdsCoordinates, "Coding Sequence Coordinates must not be null");
    }

    public static GencodeCodingTranscript of(GenomicRegion location,
                                             TranscriptIdentifier id,
                                             Coordinates cdsCoordinates,
                                             Biotype biotype,
                                             EvidenceLevel evidenceLevel,
                                             List<Coordinates> exons,
                                             Set<String> tags) {
        return new GencodeCodingTranscript(location, id, cdsCoordinates, biotype, evidenceLevel, exons, tags);
    }

    @Override
    public Coordinates cdsCoordinates() {
        return cdsCoordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GencodeCodingTranscript that = (GencodeCodingTranscript) o;
        return Objects.equals(cdsCoordinates, that.cdsCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cdsCoordinates);
    }

    @Override
    public String toString() {
        return "CodingTranscript{" +
                "cdsCoordinates=" + cdsCoordinates +
                '}';
    }
}
