package org.monarchinitiative.sgenes.simple;

import org.monarchinitiative.sgenes.model.*;
import org.monarchinitiative.svart.*;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;

import java.util.List;

public class Genes {

    private static final GenomicAssembly ASSEMBLY = GenomicAssemblies.GRCh38p13();

    private Genes() {

    }
    // ------------------------------------------------- SURF2 ---------------------------------------------------------

    public static Gene surf2() {
        Contig contig = ASSEMBLY.contigByName("chr9");

        Transcript surf2_201 = surf2_201(contig);
        List<Transcript> transcripts = List.of(surf2_201);

        GenomicRegion location = GenomicRegion.of(contig, Strand.POSITIVE, CoordinateSystem.zeroBased(), 133_356_549, 133_361_158);
        GeneIdentifier id = GeneIdentifier.of("ENSG00000148291.10", "SURF2", "HGNC:11475", "NCBIGene:6835");
        return Gene.of(id, location, transcripts);
    }

    public static Transcript surf2_201(Contig contig) {
        TranscriptIdentifier txId = TranscriptIdentifier.of("ENST00000371964.5", "SURF2-201", "CCDS6967.1");
        GenomicRegion location = GenomicRegion.of(contig, Strand.POSITIVE, CoordinateSystem.zeroBased(), 133_356_549, 133_361_158);
        List<Coordinates> exons = List.of(
                Coordinates.of(CoordinateSystem.zeroBased(), 133_356_549, 133_356_670),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_356_913, 133_357_068),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_357_710, 133_357_814),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_359_949, 133_360_129),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_360_264, 133_360_434),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_361_055, 133_361_158)
        );

        return Transcript.of(txId, location, exons, location.coordinates(), TranscriptMetadata.of(TranscriptEvidence.MANUAL_ANNOTATION));
    }

    // ------------------------------------------------- FBN1 ----------------------------------------------------------

    public static Gene surf1() {
        Contig contig = ASSEMBLY.contigByName("chr9");

        GeneIdentifier id = GeneIdentifier.of("ENSG00000148290.10", "SURF1", "HGNC:11474", "NCBIGene:6834");
        GenomicRegion location = GenomicRegion.of(contig, Strand.POSITIVE, CoordinateSystem.zeroBased(), 133_351_757, 133_356_676).toNegativeStrand();

        Transcript surf1_201 = surf1_201(contig);
        Transcript surf1_205 = surf1_205(contig);
        List<Transcript> transcripts = List.of(surf1_201, surf1_205);
        return Gene.of(id, location, transcripts);
    }

    public static Transcript surf1_201(Contig contig) {
        TranscriptIdentifier txId = TranscriptIdentifier.of("ENST00000371974.8", "SURF1-201", "CCDS6966.1");
        GenomicRegion location = GenomicRegion.of(contig, Strand.POSITIVE, CoordinateSystem.zeroBased(), 133_351_757, 133_356_487).toNegativeStrand();
        List<Coordinates> exons = List.of(
                Coordinates.of(CoordinateSystem.zeroBased(), 133_356_399, 133_356_487).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_356_268, 133_356_320).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_354_823, 133_354_957).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_354_658, 133_354_741).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_353_748, 133_353_940).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_352_693, 133_352_766).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_352_445, 133_352_608).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_352_060, 133_352_142).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_351_757, 133_351_982).invert(contig)
                );
        Coordinates cdsCoordinates = Coordinates.of(CoordinateSystem.zeroBased(), 133_351_912, 133_356_453).invert(contig);
        return Transcript.of(txId, location, exons, cdsCoordinates, TranscriptMetadata.of(TranscriptEvidence.MANUAL_ANNOTATION));
    }

    public static Transcript surf1_205(Contig contig) {
        TranscriptIdentifier txId = TranscriptIdentifier.of("ENST00000615505.4", "SURF1-205", "CCDS75928.1");
        GenomicRegion location = GenomicRegion.of(contig, Strand.POSITIVE, CoordinateSystem.zeroBased(), 133_351_804, 133_356_485).toNegativeStrand();
        List<Coordinates> exons = List.of(
                Coordinates.of(CoordinateSystem.zeroBased(), 133_356_399, 133_356_485).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_354_823, 133_354_957).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_354_658, 133_354_741).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_353_748, 133_353_940).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_352_693, 133_352_766).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_352_445, 133_352_608).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_352_060, 133_352_142).invert(contig),
                Coordinates.of(CoordinateSystem.zeroBased(), 133_351_804, 133_351_982).invert(contig)
        );

        Coordinates cdsCoordinates = Coordinates.of(CoordinateSystem.zeroBased(), 133_351_912, 133_353_936).invert(contig);

        return Transcript.of(txId, location, exons, cdsCoordinates, TranscriptMetadata.of(TranscriptEvidence.AUTOMATED_ANNOTATION));
    }

}
