package xyz.ielis.silent.genes.model;

/**
 * Identifier for an entity (e.g. gene, transcript).
 * <p>
 * The identifier provides <code>accession</code> and <code>symbol</code>, where <code>accession</code> is a unique ID
 * of the entity to be used by machine, and <code>symbol</code> is intended for humans.
 */
public interface Identifier {

    /**
     * @return a unique ID of the entity meant to be used by a machine (e.g. <code>ENSG00000148290.10</code>, <code>ENST00000371974.8</code>).
     */
    String accession();

    /**
     * @return entity ID intended for humans (e.g. <code>SURF1</code>)
     */
    String symbol();

}
