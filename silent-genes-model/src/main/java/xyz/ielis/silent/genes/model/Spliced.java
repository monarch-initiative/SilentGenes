package xyz.ielis.silent.genes.model;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The interface to represent entities that are spliced into one or more {@link Transcript}s.
 */
public interface Spliced {

    Iterator<? extends Transcript> transcripts();

    int transcriptCount();

    default Stream<? extends Transcript> transcriptStream() {
        return StreamSupport.stream(Spliterators.spliterator(transcripts(), transcriptCount(),
                Spliterator.DISTINCT & Spliterator.SIZED), false);
    }

    default Stream<? extends CodingTranscript> codingTranscripts() {
        return transcriptStream()
                .filter(Transcript::isCoding)
                .map(CodingTranscript.class::cast);
    }


}
