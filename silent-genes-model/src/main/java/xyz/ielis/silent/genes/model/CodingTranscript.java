package xyz.ielis.silent.genes.model;

import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface CodingTranscript extends Coding, Transcript {

    default List<Coordinates> fivePrimeRegion() {
        List<Coordinates> utrCoordinates = new ArrayList<>(exons().size());
        int cdsStart = cdsCoordinates().startWithCoordinateSystem(CoordinateSystem.zeroBased());
        for (Coordinates exon : exons()) {
            if (cdsStart >= exon.startWithCoordinateSystem(CoordinateSystem.zeroBased())) {
                if (cdsStart != exon.startWithCoordinateSystem(CoordinateSystem.zeroBased())) {
                    utrCoordinates.add(Coordinates.of(coordinateSystem(), exon.start(), cdsCoordinates().startWithCoordinateSystem(CoordinateSystem.zeroBased())));
                }
                break;
            } else {
                utrCoordinates.add(exon);
            }
        }

        return utrCoordinates;
    }

    default List<Coordinates> threePrimeRegion() {
        List<Coordinates> utrCoordinates = new ArrayList<>(exons().size());
        int cdsEnd = cdsCoordinates().endWithCoordinateSystem(CoordinateSystem.zeroBased());
        List<Coordinates> exons = exons();
        for (int i = exons.size() - 1; i >= 0; i--) {
            Coordinates exon = exons.get(i);
            if (cdsEnd > exon.startWithCoordinateSystem(CoordinateSystem.zeroBased())) {
                utrCoordinates.add(Coordinates.of(CoordinateSystem.zeroBased(), cdsEnd, exon.end()));
            } else {
                utrCoordinates.add(exon);
            }
        }
        Collections.reverse(utrCoordinates);

        return utrCoordinates;
    }

    //   |------------|
    //     |-------|
    //   0,1,2
    //   1,2,3,...,
    ///  0,1,2,...,9,10
    ///
    //  (0,10)  fully open
    //  [1,9]  fully closed
    //  (0,9]  left open
    //  [1,10) right open <-- THE ONE
}
