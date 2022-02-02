package org.monarchinitiative.sgenes.model;

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
            if (exon.endWithCoordinateSystem(CoordinateSystem.zeroBased()) > cdsStart) {
                if (cdsStart != exon.startWithCoordinateSystem(CoordinateSystem.zeroBased())) {
                    Coordinates coordinates = Coordinates.of(CoordinateSystem.zeroBased(), exon.startWithCoordinateSystem(CoordinateSystem.zeroBased()), cdsStart);
                    utrCoordinates.add(coordinates);
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
                utrCoordinates.add(Coordinates.of(CoordinateSystem.zeroBased(), cdsEnd, exon.endWithCoordinateSystem(CoordinateSystem.zeroBased())));
                break;
            } else {
                utrCoordinates.add(exon);
            }
        }
        Collections.reverse(utrCoordinates);

        return utrCoordinates;
    }
}
