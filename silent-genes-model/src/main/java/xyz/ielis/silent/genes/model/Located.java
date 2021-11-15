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

    default int contigId() {
        return location().contig().id();
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

    default int startOnStrand(Strand strand) {
        return location().startOnStrand(strand);
    }

    default int startOnStrandWithCoordinateSystem(Strand strand, CoordinateSystem target) {
        return location().startOnStrandWithCoordinateSystem(strand, target);
    }

    default int end() {
        return location().end();
    }

    default int endWithCoordinateSystem(CoordinateSystem target) {
        return location().endWithCoordinateSystem(target);
    }

    default int endOnStrand(Strand strand) {
        return location().endOnStrand(strand);
    }

    default int endOnStrandWithCoordinateSystem(Strand strand, CoordinateSystem target) {
        return location().endOnStrandWithCoordinateSystem(strand, target);
    }

    default Coordinates coordinates() {
        return location().coordinates();
    }

}
