package org.monarchinitiative.sgenes.gtf.model.impl.gencode;

import org.monarchinitiative.sgenes.gtf.model.*;
import org.monarchinitiative.sgenes.model.base.BaseGene;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.GeneIdentifier;

import java.util.*;

public class GencodeGeneImpl extends BaseGene implements GencodeGene {

    private final List<? extends GencodeTranscript> transcripts;
    private final GencodeTranscriptMetadata metadata;

    public static GencodeGeneImpl of(GeneIdentifier id,
                                     GenomicRegion location,
                                     List<GencodeTranscript> transcripts,
                                     GencodeTranscriptMetadata metadata) {
        return new GencodeGeneImpl(id, location, transcripts, metadata);
    }

    private GencodeGeneImpl(GeneIdentifier id,
                            GenomicRegion location,
                            List<GencodeTranscript> transcripts,
                            GencodeTranscriptMetadata metadata) {
        super(id, location);
        this.transcripts = Objects.requireNonNull(transcripts, "Transcripts must not be null");
        if (this.transcripts.isEmpty())
            throw new IllegalArgumentException("Transcripts must not be empty");
        this.metadata = Objects.requireNonNull(metadata, "Gencode metadata must not be null");

    }

    @Override
    public GencodeTranscriptMetadata gencodeMetadata() {
        return metadata;
    }

    @Override
    public Biotype biotype() {
        return metadata.biotype();
    }

    @Override
    public EvidenceLevel evidenceLevel() {
        return metadata.evidenceLevel();
    }

    @Override
    public Set<String> tags() {
        return metadata.tags();
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
        return Objects.equals(transcripts, that.transcripts) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transcripts, metadata);
    }

    @Override
    public String toString() {
        return "GencodeGeneImpl{" +
                "id=" + id +
                ", location=" + location +
                ", transcripts=" + transcripts +
                ", gencodeMetadata=" + metadata +
                "}";
    }
}
