package xyz.ielis.silent.genes.model;

import java.util.stream.Stream;

/**
 * The interface to represent entities that are spliced into one or more {@link Transcript}s.
 */
public interface Spliced {

    Stream<? extends Transcript> transcripts();

    default int transcriptCount() {
        return Math.toIntExact(transcripts().count());
    }

}
