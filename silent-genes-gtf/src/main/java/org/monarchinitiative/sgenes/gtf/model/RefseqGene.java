package org.monarchinitiative.sgenes.gtf.model;

import org.monarchinitiative.sgenes.model.Gene;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface RefseqGene extends Gene {

    Biotype biotype();

    @Override
    Iterator<? extends RefseqTranscript> transcripts();

    @Override
    default Stream<? extends RefseqTranscript> transcriptStream() {
        return StreamSupport.stream(Spliterators.spliterator(transcripts(), transcriptCount(),
                Spliterator.DISTINCT & Spliterator.SIZED), false);
    }
}
