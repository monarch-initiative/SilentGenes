package xyz.ielis.silent.genes.simple;

import org.monarchinitiative.svart.*;
import xyz.ielis.silent.genes.model.Gene;
import xyz.ielis.silent.genes.model.GeneIdentifier;
import xyz.ielis.silent.genes.model.Transcript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

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
        Coordinates startCds = Coordinates.of(CoordinateSystem.zeroBased(), 133_356_592, 133_356_595);
        Coordinates stopCds = Coordinates.of(CoordinateSystem.zeroBased(), 133_361_136, 133_361_139);
        Coordinates fivePrimeRegion = Coordinates.of(CoordinateSystem.zeroBased(), 133_356_454, 133_356_487).invert(contig);
        Coordinates threePrimeRegion = Coordinates.of(CoordinateSystem.zeroBased(), 133_351_758, 133_351_913).invert(contig);

        return Transcript.coding(txId, location, exons, startCds, stopCds, fivePrimeRegion, threePrimeRegion);
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
        Coordinates startCds = Coordinates.of(CoordinateSystem.zeroBased(), 133_356_450, 133_356_453).invert(contig);
        Coordinates stopCds = Coordinates.of(CoordinateSystem.zeroBased(), 133_351_912, 133_351_915).invert(contig);
        Coordinates fivePrimeRegion = Coordinates.of(CoordinateSystem.zeroBased(), 133_356_454, 133_356_487).invert(contig);
        Coordinates threePrimeRegion = Coordinates.of(CoordinateSystem.zeroBased(), 133_351_758, 133_351_913).invert(contig);
        return Transcript.coding(txId, location, exons, startCds, stopCds, fivePrimeRegion, threePrimeRegion);
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

        Coordinates startCds = Coordinates.of(CoordinateSystem.zeroBased(), 133_356_450, 133_356_453).invert(contig);
        Coordinates stopCds = Coordinates.of(CoordinateSystem.zeroBased(), 133_351_912, 133_351_915).invert(contig);
        Coordinates fivePrimeRegion = Coordinates.of(CoordinateSystem.zeroBased(), 133_356_454, 133_356_487).invert(contig);
        Coordinates threePrimeRegion = Coordinates.of(CoordinateSystem.zeroBased(), 133_351_758, 133_351_913).invert(contig);

        return Transcript.coding(txId, location, exons, startCds, stopCds, fivePrimeRegion, threePrimeRegion);
    }

}
