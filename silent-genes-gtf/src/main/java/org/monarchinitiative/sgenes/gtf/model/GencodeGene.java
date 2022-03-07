package org.monarchinitiative.sgenes.gtf.model;

import org.monarchinitiative.sgenes.model.Gene;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface GencodeGene extends Gene {

    // TODO - evaluate how useful is it to have metadata on the gene level
    Biotype biotype();

    // TODO - evaluate how useful is it to have metadata on the gene level
    EvidenceLevel evidenceLevel();

    // TODO - evaluate how useful is it to have metadata on the gene level
    Set<String> tags();

    @Override
    Iterator<? extends GencodeTranscript> transcripts();

    @Override
    default Stream<? extends GencodeTranscript> transcriptStream() {
        return StreamSupport.stream(Spliterators.spliterator(transcripts(), transcriptCount(),
                Spliterator.DISTINCT & Spliterator.SIZED), false);
    }
}
