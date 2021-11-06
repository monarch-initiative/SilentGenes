package xyz.ielis.gtram.model;

/**
 * The interface to represent (non)coding transcript. The transcript is located on reference genome, it consists
 * of exons and introns, and can be {@link Coding} or non-coding.
 */
public interface Transcript extends Located, Spliceable {

    default boolean isCoding() {
        return this instanceof Coding;
    }

}
