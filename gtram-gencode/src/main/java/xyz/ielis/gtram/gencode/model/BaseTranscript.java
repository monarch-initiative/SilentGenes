package xyz.ielis.gtram.gencode.model;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;

import java.util.List;
import java.util.Objects;

abstract class BaseTranscript implements GencodeTranscript {

    private final GenomicRegion location;
    private final String id;
    private final String type;
    private final Status status;
    private final String name;
    private final EvidenceLevel evidenceLevel;
    private final List<Coordinates> exons;

    protected BaseTranscript(GenomicRegion location,
                             String id,
                             String type,
                             Status status,
                             String name,
                             EvidenceLevel evidenceLevel,
                             List<Coordinates> exons) {
        this.location = location;
        this.id = id;
        this.type = type;
        this.status = status;
        this.name = name;
        this.evidenceLevel = evidenceLevel;
        this.exons = exons;
    }


    @Override
    public GenomicRegion location() {
        return location;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public EvidenceLevel evidenceLevel() {
        return evidenceLevel;
    }

    @Override
    public List<Coordinates> exons() {
        return exons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTranscript that = (BaseTranscript) o;
        return Objects.equals(location, that.location) && Objects.equals(id, that.id) && Objects.equals(type, that.type) && status == that.status && Objects.equals(name, that.name) && evidenceLevel == that.evidenceLevel && Objects.equals(exons, that.exons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, id, type, status, name, evidenceLevel, exons);
    }

    @Override
    public String toString() {
        return "BaseTranscript{" +
                "location=" + location +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", evidenceLevel=" + evidenceLevel +
                ", exons=" + exons +
                '}';
    }

}
