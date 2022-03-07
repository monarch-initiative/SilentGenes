package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.impl.CodingTranscriptDefault;
import org.monarchinitiative.sgenes.model.impl.TranscriptDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The interface to represent a gene transcript.
 * <p>
 * The transcript is located in the reference genome, it consists of exons and introns, and it can be {@link Coding}.
 * <p>
 * The implementors must ensure that the exons are arranged in ascending order and the exons do not overlap.
 * </ul>
 */
public interface Transcript extends Located, Identified<TranscriptIdentifier>, Annotated<TranscriptMetadata> {

    /**
     * Create a new {@link Transcript} instance.
     *
     * @param id             transcript ID
     * @param location       transcript location
     * @param exons          exon coordinates
     * @param cdsCoordinates coordinates of the coding region, or <code>null</code> for non-coding transcript.
     *                       The expected <code>cdsCoordinates</code> spans the coordinates
     *                       delimited by start and stop codons (included), including intronic sequences.
     * @param metadata       transcript metadata
     * @return transcript instance
     */
    static Transcript of(TranscriptIdentifier id,
                         GenomicRegion location,
                         List<Coordinates> exons,
                         Coordinates cdsCoordinates,
                         TranscriptMetadata metadata) {
        Objects.requireNonNull(exons, "Exons must not be null");
        if (exons.isEmpty())
            throw new IllegalArgumentException("Exon list must not be empty");
        if (cdsCoordinates == null)
            return TranscriptDefault.of(id, location, exons, metadata);
        else
            return CodingTranscriptDefault.of(id, location, exons, cdsCoordinates, metadata);
    }

    /**
     * @deprecated use {@link #of(TranscriptIdentifier, GenomicRegion, List, Coordinates, TranscriptMetadata)} and pass null for <code>cdsCoordinates</code>
     */
    @Deprecated(since = "0.2.1", forRemoval = true)
    static Transcript noncoding(TranscriptIdentifier id,
                                GenomicRegion location,
                                List<Coordinates> exons) {
        return of(id, location, exons, null, TranscriptMetadata.of(null));
    }

    /**
     * Use {@link #of(TranscriptIdentifier, GenomicRegion, List, Coordinates, TranscriptMetadata)}
     */
    @Deprecated(since = "0.2.1", forRemoval = true)
    static CodingTranscript coding(TranscriptIdentifier id,
                                   GenomicRegion location,
                                   List<Coordinates> exons,
                                   Coordinates cdsCoordinates) {
        return (CodingTranscript) of(id, location, exons, cdsCoordinates, TranscriptMetadata.of(null));
    }

    private static List<Coordinates> computeIntronLocations(List<Coordinates> exons) {
        if (exons.size() == 1) { // shortcut
            return List.of();
        }

        ArrayList<Coordinates> introns = new ArrayList<>(exons.size() - 1);
        int intronStart = exons.get(0).endWithCoordinateSystem(CoordinateSystem.zeroBased());
        for (int i = 1; i < exons.size(); i++) { // start from the 2nd exon
            Coordinates exon = exons.get(i);
            int intronEnd = exon.startWithCoordinateSystem(CoordinateSystem.zeroBased());
            introns.add(Coordinates.of(exon.coordinateSystem(), intronStart, intronEnd));
            intronStart = exon.endWithCoordinateSystem(CoordinateSystem.zeroBased());
        }
        return List.copyOf(introns);
    }

    List<Coordinates> exons();

    default int exonCount() {
        return exons().size();
    }

    default List<Coordinates> introns() {
        return computeIntronLocations(exons());
    }

    default int intronCount() {
        // should not be negative as we check the presence of >=1 exons in Transcript.of()
        return exons().size() - 1;
    }

    default boolean isCoding() {
        return this instanceof CodingTranscript;
    }

}
