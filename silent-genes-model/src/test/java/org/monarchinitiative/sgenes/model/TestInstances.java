package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.svart.*;
import org.monarchinitiative.svart.assembly.AssignedMoleculeType;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.assembly.SequenceRole;

import java.util.List;

public class TestInstances {

    public static class Toy {
        public static final Contig CONTIG = Contig.of(1, "1", SequenceRole.ASSEMBLED_MOLECULE, "1", AssignedMoleculeType.CHROMOSOME, 1000, "", "", "");

        public static CodingTranscript toy() {
            TranscriptIdentifier txId = TranscriptIdentifier.of("TOY_TX", "TOY", null);
            GenomicRegion location = GenomicRegion.of(CONTIG, Strand.POSITIVE, CoordinateSystem.zeroBased(), 100, 600);
            List<Coordinates> exons = List.of(
                    Coordinates.of(CoordinateSystem.zeroBased(), 100, 200),
                    Coordinates.of(CoordinateSystem.zeroBased(), 300, 400),
                    Coordinates.of(CoordinateSystem.zeroBased(), 500, 700),
                    Coordinates.of(CoordinateSystem.zeroBased(), 800, 850),
                    Coordinates.of(CoordinateSystem.zeroBased(), 900, 950)
            );
            Coordinates cdsCoordinates = Coordinates.of(CoordinateSystem.zeroBased(), 150, 825);
            return (CodingTranscript) Transcript.of(txId, location, exons, cdsCoordinates);
        }
    }


    public static class RealWorld {
        public static final GenomicAssembly GRCH38 = GenomicAssemblies.GRCh38p13();
        private static final Contig contig = GRCH38.contigByName("chr17");

        public static CodingTranscript tp53() {
            TranscriptIdentifier txId = TranscriptIdentifier.of("ENST00000504937.5", "TP53",
                    "CCDS73967.1");
            GenomicRegion location = GenomicRegion.of(contig, Strand.POSITIVE, CoordinateSystem.zeroBased(),
                    7668402, 7675493).toNegativeStrand();
            List<Coordinates> exons = List.of(
                    Coordinates.of(CoordinateSystem.zeroBased(), 7675052, 7675493).invert(contig),
                    Coordinates.of(CoordinateSystem.zeroBased(), 7674858, 7674971).invert(contig),
                    Coordinates.of(CoordinateSystem.zeroBased(), 7674180, 7674290).invert(contig),
                    Coordinates.of(CoordinateSystem.zeroBased(), 7673700, 7673837).invert(contig),
                    Coordinates.of(CoordinateSystem.zeroBased(), 7673534, 7673608).invert(contig),
                    Coordinates.of(CoordinateSystem.zeroBased(), 7670608, 7670715).invert(contig),
                    Coordinates.of(CoordinateSystem.zeroBased(), 7668402, 7669690).invert(contig)
            );
            Coordinates cdsCoordinates = Coordinates.of(CoordinateSystem.zeroBased(), 7669608, 7675215).invert(contig);
            return (CodingTranscript) Transcript.of(txId, location, exons, cdsCoordinates);
        }
    }

}
