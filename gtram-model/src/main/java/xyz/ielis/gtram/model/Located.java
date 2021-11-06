package xyz.ielis.gtram.model;

import org.monarchinitiative.svart.Contig;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.svart.Strand;

/**
 * The interface to represent entities that are located in the reference genome.
 */
public interface Located {

    GenomicRegion location();

    // derived methods

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


}
