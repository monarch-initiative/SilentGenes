package xyz.ielis.silent.genes.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.*;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CodingTranscriptTest {

    private static final GenomicAssembly ASSEMBLY = GenomicAssemblies.GRCh38p13();

    @Nested
    public static class Simple {

        private CodingTranscript transcript;
        private final Contig contig = ASSEMBLY.contigByName("chr17");

        @BeforeEach
        public void setUp(){

            transcript = this.getTranscript(contig);
        }

        @Test
        public void fivePrimeRegion() {
            List<Coordinates> fivePrimeRegion = transcript.fivePrimeRegion();
            assertThat(fivePrimeRegion, hasSize(1));
            assertThat(fivePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(), 7668402, 7675493).invert(contig)));
        }

        @Test
        public void threePrimeRegion() {
            List<Coordinates> threePrimeRegion = transcript.threePrimeRegion();
            assertThat(threePrimeRegion, hasSize(1));
            assertThat(threePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(),7668402,7669608).invert(contig)));
        }

        private CodingTranscript getTranscript(Contig contig){
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
            return Transcript.coding(txId, location, exons, cdsCoordinates);
        }

    }

    @Nested
    public static class Complex {
        private CodingTranscript transcript;
        private final Contig contig = ASSEMBLY.contigByName("chr17");

        @BeforeEach
        public void setUp() {
            transcript = this.getTranscript(contig);
        }

        @Test
        public void fivePrimeRegion() {
            List<Coordinates> fivePrimeRegion = transcript.fivePrimeRegion();
            assertThat(fivePrimeRegion, hasSize(2));
            assertThat(fivePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(), 0, 100)));
            assertThat(fivePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(), 175, 177)));
        }

        @Test
        public void theePrimeRegion() {
            List<Coordinates> threePrimeRegion = transcript.threePrimeRegion();
            assertThat(threePrimeRegion, hasSize(2));
            assertThat(threePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(), 270, 275)));
            assertThat(threePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(), 300, 500)));
        }


        private CodingTranscript getTranscript(Contig contig){
            TranscriptIdentifier txId = TranscriptIdentifier.of("ENST00000504937.5", "TP53",
                    "CCDS73967.1");
            GenomicRegion location = GenomicRegion.of(contig, Strand.POSITIVE, CoordinateSystem.zeroBased(),
                    0, 500);
            List<Coordinates> exons = List.of(
                    Coordinates.of(CoordinateSystem.zeroBased(), 0, 100),
                    Coordinates.of(CoordinateSystem.zeroBased(), 175, 275),
                    Coordinates.of(CoordinateSystem.zeroBased(), 300, 500)
            );
            Coordinates cdsCoordinates = Coordinates.of(CoordinateSystem.zeroBased(), 177, 270);
            return Transcript.coding(txId, location, exons, cdsCoordinates);
        }
    }



}