package org.monarchinitiative.sgenes.model;

import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.impl.CodingTranscriptDefault;
import org.monarchinitiative.sgenes.model.impl.TranscriptDefault;

import java.util.List;

/**
 * The interface to represent a gene transcript.
 * <p>
 * The transcript is located in the reference genome, it consists of exons and introns, and it can be {@link Coding}.
 * <p>
 * The implementors must ensure that the exons are arranged in ascending order and the exons do not overlap.
 * </ul>
 */
public interface Transcript extends Located, Identified<TranscriptIdentifier> {

    /**
     * Create a new {@link Transcript} instance.
     *
     * @param id transcript ID
     * @param location transcript location
     * @param exons exon coordinates
     * @param cdsCoordinates coordinates of the coding region, or <code>null</code> for non-coding transcript.
     *                       The expected <code>cdsCoordinates</code> spans the coordinates
     *                       delimited by start and stop codons (included), including intronic sequences.
     * @return transcript instance
     */
    static Transcript of(TranscriptIdentifier id,
                         GenomicRegion location,
                         List<Coordinates> exons,
                         Coordinates cdsCoordinates) {
        if (cdsCoordinates == null)
            return TranscriptDefault.of(id, location, exons);
        else
            return CodingTranscriptDefault.of(id, location, exons, cdsCoordinates);
    }

    /**
     * Use {@link #of(TranscriptIdentifier, GenomicRegion, List, Coordinates)} and pass null for <code>cdsCoordinates</code>
     */
    @Deprecated
    static Transcript noncoding(TranscriptIdentifier id,
                                GenomicRegion location,
                                List<Coordinates> exons) {
        return TranscriptDefault.of(id, location, exons);
    }

    /**
     * Use {@link #of(TranscriptIdentifier, GenomicRegion, List, Coordinates)}
     */
    @Deprecated
    static CodingTranscript coding(TranscriptIdentifier id,
                             GenomicRegion location,
                             List<Coordinates> exons,
                             Coordinates cdsCoordinates) {
        return CodingTranscriptDefault.of(id, location, exons, cdsCoordinates);
    }

    List<Coordinates> exons();

    default boolean isCoding() {
        return this instanceof CodingTranscript;
    }

}
