package org.monarchinitiative.sgenes.gtf.model.impl.gencode;

import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.gtf.model.GencodeMetadata;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscript;
import org.monarchinitiative.sgenes.model.base.BaseGene;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.GeneIdentifier;

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
                "id=" + id +
                ", location=" + location +
                ", transcripts=" + transcripts +
                ", gencodeMetadata=" + gencodeMetadata +
                "}";
    }
}
