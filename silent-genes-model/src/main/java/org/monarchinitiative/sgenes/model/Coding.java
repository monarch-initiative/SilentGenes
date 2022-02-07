package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;

import java.util.List;

/**
 * The interface to represent coding entities, entities that have start/initiation and stop/termination codons.
 * <p>
 * Note, the implementations must ensure that the start codon is located upstream of the stop codon (at lower coordinates),
 * and that the codons span 3 bases.
 */
public interface Coding {


    Coordinates cdsCoordinates();

    default Coordinates startCodon() {
        return Coordinates.of(cdsCoordinates().coordinateSystem(), cdsCoordinates().start(), cdsCoordinates().end() + 3);
    }

    default Coordinates stopCodon() {
        return Coordinates.of(cdsCoordinates().coordinateSystem(), cdsCoordinates().end() - 3, cdsCoordinates().end());
    }

    default int codingStart() {
        return startCodon().start();
    }

    default int codingStartWithCoordinateSystem(CoordinateSystem target) {
        return startCodon().startWithCoordinateSystem(target);
    }

    default int codingEnd() {
        return stopCodon().end() - stopCodon().length();
    }

    default int codingEndWithCoordinateSystem(CoordinateSystem target) {
        return stopCodon().endWithCoordinateSystem(target) - stopCodon().length();
    }

    default int cdsLength() {
        if (this instanceof Transcript) {
            return cdsLength(startCodon(), stopCodon(), ((Transcript) this).exons());
        } else {
            return stopCodon().endWithCoordinateSystem(CoordinateSystem.zeroBased())
                    - startCodon().startWithCoordinateSystem(CoordinateSystem.zeroBased())
                    - stopCodon().length(); // the termination codon is not part of the CDS!
        }
    }

    private int cdsLength(Coordinates startCodon, Coordinates stopCodon, List<Coordinates> exons) {
        int cdsLength = 0;
        for (Coordinates exon : exons) {
            int start = Math.max(
                    Math.min(startCodon.startWithCoordinateSystem(CoordinateSystem.zeroBased()), exon.endWithCoordinateSystem(CoordinateSystem.zeroBased())),
                    exon.startWithCoordinateSystem(CoordinateSystem.zeroBased()));
            int end = Math.min(
                    Math.max(stopCodon.endWithCoordinateSystem(CoordinateSystem.zeroBased()), exon.startWithCoordinateSystem(CoordinateSystem.zeroBased())),
                    exon.endWithCoordinateSystem(CoordinateSystem.zeroBased()));
            cdsLength += end - start;
        }
        return cdsLength - stopCodon.length(); // the termination codon is not part of the CDS!
    }

}
