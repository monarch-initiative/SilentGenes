package xyz.ielis.gtram.model;

import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;

/**
 * The interface to represent entities that are coding, i.e. coordinates of the start/initiation and stop/termination codons are known.
 * <p>
 * Note, the implementations must ensure that the start codon is located upstream of the stop codon (at lower coordinates),
 * and that the codons span 3 bases.
 */
public interface Coding {

    Coordinates startCodon();

    Coordinates stopCodon();

    default int cdsLength() {
        if (this instanceof Spliceable) {
            return TxUtils.cdsLength(startCodon(), stopCodon(), ((Spliceable) this).exons());
        } else {
            return stopCodon().endWithCoordinateSystem(CoordinateSystem.zeroBased())
                    - startCodon().startWithCoordinateSystem(CoordinateSystem.zeroBased())
                    - stopCodon().length(); // the termination codon is not part of the CDS!
        }
    }

}
