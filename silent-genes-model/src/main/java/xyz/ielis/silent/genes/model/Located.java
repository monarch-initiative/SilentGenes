package xyz.ielis.silent.genes.model;

import org.monarchinitiative.svart.*;

/**
 * The interface to represent entities that are located in the reference genome.
 */
public interface Located {

    GenomicRegion location();

    // --------------------------------------- derived methods ---------------------------------------------------------

    default Contig contig() {
        return location().contig();
    }

    default String contigName() {
        return location().contigName();
    }

    default Strand strand() {
        return location().strand();
    }

    default CoordinateSystem coordinateSystem() {
        return location().coordinateSystem();
    }

    default int start() {
        return location().start();
    }

    default int startWithCoordinateSystem(CoordinateSystem target) {
        return location().startWithCoordinateSystem(target);
    }

    default int end() {
        return location().end();
    }

    default int endWithCoordinateSystem(CoordinateSystem target) {
        return location().endWithCoordinateSystem(target);
    }

    default Coordinates coordinates() {
        return location().coordinates();
    }

}
