package xyz.ielis.gtram.gencode.model;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.gtram.model.Coding;

import java.util.List;
import java.util.Objects;

// TODO - does this have to be public?
public class CodingTranscript extends BaseTranscript implements Coding {

    private final Coordinates startCodon;
    private final Coordinates stopCodon;

    private CodingTranscript(GenomicRegion location,
                             Coordinates startCodon,
                             Coordinates stopCodon,
                             String id,
                             String type,
                             Status status,
                             String name,
                             EvidenceLevel evidenceLevel,
                             List<Coordinates> exons) {
        super(location, id, type, status, name, evidenceLevel, exons);
        this.startCodon = startCodon;
        this.stopCodon = stopCodon;
    }

    public static CodingTranscript of(GenomicRegion location,
                                      Coordinates startCodon,
                                      Coordinates stopCodon,
                                      String id,
                                      String type,
                                      Status status,
                                      String name,
                                      EvidenceLevel evidenceLevel,
                                      List<Coordinates> exons) {
        // TODO - null checks
        return new CodingTranscript(location, startCodon, stopCodon, id, type, status, name, evidenceLevel, exons);
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
