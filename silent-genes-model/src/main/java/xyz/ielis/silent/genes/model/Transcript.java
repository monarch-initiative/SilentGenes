package xyz.ielis.silent.genes.model;

/**
 * The interface to represent (non)coding transcript. The transcript is located in reference genome, it consists
 * of exons and introns, and can be {@link Coding} or non-coding.
 */
public interface Transcript extends Located, Spliced {

    // TODO - add identifiers

    default boolean isCoding() {
        return this instanceof Coding;
    }

}
