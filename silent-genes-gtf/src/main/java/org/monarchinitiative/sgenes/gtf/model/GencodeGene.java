package org.monarchinitiative.sgenes.gtf.model;

import org.monarchinitiative.sgenes.model.Gene;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface GencodeGene extends Gene, GencodeAnnotated {

    @Override
    Iterator<? extends GencodeTranscript> transcripts();

    @Override
    default Stream<? extends GencodeTranscript> transcriptStream() {
        return StreamSupport.stream(Spliterators.spliterator(transcripts(), transcriptCount(),
                Spliterator.DISTINCT & Spliterator.SIZED), false);
    }
}
