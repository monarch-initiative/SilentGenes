package xyz.ielis.gtram.model;

import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;

import java.util.List;

class TxUtils {

    private static final CoordinateSystem CS = CoordinateSystem.zeroBased();

    private TxUtils() {
    }

    static int cdsLength(Coordinates startCodon, Coordinates stopCodon, List<Coordinates> exons) {
        int cdsLength = 0;
        for (Coordinates exon : exons) {
            int end = Math.min(
                    Math.max(stopCodon.endWithCoordinateSystem(CS), exon.startWithCoordinateSystem(CS)),
                    exon.endWithCoordinateSystem(CS));
            int start = Math.max(
                    Math.min(startCodon.startWithCoordinateSystem(CS), exon.endWithCoordinateSystem(CS)),
                    exon.startWithCoordinateSystem(CS));

            cdsLength += end - start;
        }
        // the termination codon is not part of the CDS!
        return cdsLength - 3;
    }

}
