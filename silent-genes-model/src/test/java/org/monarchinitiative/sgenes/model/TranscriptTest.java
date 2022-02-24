package org.monarchinitiative.sgenes.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TranscriptTest {

    @Nested
    public class ToyTest {

        private Transcript transcript;

        @BeforeEach
        public void setUp() {
            transcript = TestInstances.Toy.toy();
        }

        @Test
        public void introns() {
            List<Coordinates> introns = transcript.introns();

            assertThat(introns, hasSize(4));
            assertThat(introns, hasItems(
                    Coordinates.of(CoordinateSystem.zeroBased(), 200, 300),
                    Coordinates.of(CoordinateSystem.zeroBased(), 400, 500),
                    Coordinates.of(CoordinateSystem.zeroBased(), 700, 800),
                    Coordinates.of(CoordinateSystem.zeroBased(), 850, 900)
                    ));
        }

        @Test
        public void intronCount() {
            assertThat(transcript.intronCount(), equalTo(4));
        }

        @Test
        public void exons() {
            List<Coordinates> exons = transcript.exons();

            assertThat(exons, hasSize(5));
            assertThat(exons, hasItems(
                    Coordinates.of(CoordinateSystem.zeroBased(), 100, 200),
                    Coordinates.of(CoordinateSystem.zeroBased(), 300, 400),
                    Coordinates.of(CoordinateSystem.zeroBased(), 500, 700),
                    Coordinates.of(CoordinateSystem.zeroBased(), 800, 850),
                    Coordinates.of(CoordinateSystem.zeroBased(), 900, 950)

            ));
        }

        @Test
        public void exonCount() {
            assertThat(transcript.exonCount(), equalTo(5));
        }
    }

    @Nested
    public class RealWorldTest {

        @Test
        public void introns() {
            Transcript transcript = TestInstances.RealWorld.tp53();

            List<Coordinates> introns = transcript.introns();

            assertThat(introns, hasSize(6));
            assertThat(introns, hasItems(
                    Coordinates.of(CoordinateSystem.zeroBased(), 75_582_389, 75_582_470),
                    Coordinates.of(CoordinateSystem.zeroBased(), 75_582_583, 75_583_151),
                    Coordinates.of(CoordinateSystem.zeroBased(), 75_583_261, 75_583_604),
                    Coordinates.of(CoordinateSystem.zeroBased(), 75_583_741, 75_583_833),
                    Coordinates.of(CoordinateSystem.zeroBased(), 75_583_907, 75_586_726),
                    Coordinates.of(CoordinateSystem.zeroBased(), 75_586_833, 75_587_751))
            );
        }
    }

}