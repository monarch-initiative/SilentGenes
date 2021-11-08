package xyz.ielis.silent.genes.model;

import org.monarchinitiative.svart.Coordinates;

import java.util.List;

/**
 * The interface to represent a gene transcript.
 * <p>
 * The transcript is located in the reference genome, it consists of exons and introns, and it can be {@link Coding}.
 * <p>
 * The implementors must ensure that the exons are arranged in ascending order and the exons do not overlap.
 * </ul>
 */
public interface Transcript extends Located, Identified<TranscriptIdentifier> {

    List<Coordinates> exons();

    default boolean isCoding() {
        return this instanceof Coding;
    }

}
