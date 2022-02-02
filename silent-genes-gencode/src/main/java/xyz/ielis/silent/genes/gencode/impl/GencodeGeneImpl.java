package xyz.ielis.silent.genes.gencode.impl;

import org.monarchinitiative.svart.GenomicRegion;
import xyz.ielis.silent.genes.gencode.model.*;
import xyz.ielis.silent.genes.model.GeneIdentifier;
import xyz.ielis.silent.genes.model.base.BaseGene;

import java.util.*;

public class GencodeGeneImpl extends BaseGene implements GencodeGene {

    private final List<? extends GencodeTranscript> transcripts;
    private final GencodeMetadata gencodeMetadata;

    public static GencodeGeneImpl of(GeneIdentifier id,
                                     GenomicRegion location,
                                     List<GencodeTranscript> transcripts,
                                     GencodeMetadata gencodeMetadata) {
        return new GencodeGeneImpl(id, location, transcripts, gencodeMetadata);
    }

    private GencodeGeneImpl(GeneIdentifier id,
                            GenomicRegion location,
                            List<GencodeTranscript> transcripts,
                            GencodeMetadata gencodeMetadata) {
        super(id, location);
        this.transcripts = Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (this.transcripts.isEmpty())
            throw new IllegalArgumentException("Transcripts must not be empty");
        this.gencodeMetadata = Objects.requireNonNull(gencodeMetadata, "Gencode metadata must not be null");

    }

    @Override
    public GencodeMetadata gencodeMetadata() {
        return gencodeMetadata;
    }

    @Override
    public Iterator<? extends GencodeTranscript> transcripts() {
        return transcripts.iterator();
    }

    @Override
    public int transcriptCount() {
        return transcripts.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GencodeGeneImpl that = (GencodeGeneImpl) o;
        return Objects.equals(transcripts, that.transcripts) && Objects.equals(gencodeMetadata, that.gencodeMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transcripts, gencodeMetadata);
    }

    @Override
    public String toString() {
        return "GencodeGeneImpl{" +
                "transcripts=" + transcripts +
                ", gencodeMetadata=" + gencodeMetadata +
                "} " + super.toString();
    }
}
