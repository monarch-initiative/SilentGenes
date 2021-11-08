package xyz.ielis.silent.genes.model;

/**
 * The interface to represent entities that are spliced into one or more {@link Transcript}s.
 */
public interface Spliced {

    Iterable<Transcript> transcripts();

}
