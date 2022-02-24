package org.monarchinitiative.sgenes.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CodingTranscriptTest {

    @Nested
    public static class Simple {
        private CodingTranscript transcript;

        @BeforeEach
        public void setUp() {
            transcript = TestInstances.RealWorld.tp53();
        }

        @Test
        public void fivePrimeRegion() {
            List<Coordinates> fivePrimeRegion = transcript.fivePrimeRegion();
            assertThat(fivePrimeRegion, hasSize(1));
            assertThat(fivePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(), 7675215, 7675493).invert(transcript.contig())));
        }

        @Test
        public void threePrimeRegion() {
            List<Coordinates> threePrimeRegion = transcript.threePrimeRegion();
            assertThat(threePrimeRegion, hasSize(1));
            assertThat(threePrimeRegion, hasItem(Coordinates.of(CoordinateSystem.zeroBased(),7668402,7669608).invert(transcript.contig())));
        }

    }

    @Nested
    public static class Complex {
        private CodingTranscript transcript;
        private final Contig contig = TestInstances.RealWorld.GRCH38.contigByName("chr17");

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
            return (CodingTranscript) Transcript.of(txId, location, exons, cdsCoordinates);
        }
    }


}