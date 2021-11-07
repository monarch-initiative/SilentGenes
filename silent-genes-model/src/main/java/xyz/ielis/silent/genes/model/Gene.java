package xyz.ielis.silent.genes.model;

import java.util.stream.Stream;

/**
 * Gene is a region in a genome that has at least one transcript.
 */
public interface Gene extends Located {

    // TODO - add identifiers

    Stream<Transcript> transcripts();

}
