package xyz.ielis.gtram.model;

import java.util.stream.Stream;

/**
 * Gene is a region in a genome that has at least one transcript.
 */
public interface Gene extends Located {

    Stream<Transcript> transcripts();

}
