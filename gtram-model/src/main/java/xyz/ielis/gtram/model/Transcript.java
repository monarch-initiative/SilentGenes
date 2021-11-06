package xyz.ielis.gtram.model;

/**
 * The interface to represent Gencode tra
 */
public interface Transcript extends Located, Spliceable {

    default boolean isCoding() {
        return this instanceof Coding;
    }

}
