package xyz.ielis.gtram.model;

import org.monarchinitiative.svart.Coordinates;

import java.util.List;

/**
 * The interface to represent entities that consist of exons and introns.
 * <p>
 * The implementors must ensure:
 * <ul>
 *     <li>exons are arranged in ascending order using the coordinates,</li>
 *     <li>exons do not overlap</li>
 * </ul>
 */
public interface Spliceable extends Located {

    List<Coordinates> exons();

}
