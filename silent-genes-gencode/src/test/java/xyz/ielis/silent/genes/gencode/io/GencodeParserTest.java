package xyz.ielis.silent.genes.gencode.io;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.Strand;
import xyz.ielis.silent.genes.gencode.model.Biotype;
import xyz.ielis.silent.genes.gencode.model.EvidenceLevel;
import xyz.ielis.silent.genes.gencode.model.GencodeGene;
import xyz.ielis.silent.genes.model.Coding;
import xyz.ielis.silent.genes.model.Identified;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GencodeParserTest {

    private static final GenomicAssembly GENOMIC_ASSEMBLY = GenomicAssemblies.GRCh38p13();

    // A GTF file with definitions of SURF2 and FBN1 genes from Gencode.
    private static final Path SMALL_GTF_PATH = Path.of(GencodeParserTest.class.getResource("gencode.v38.chr_patch_hapl_scaff.basic.annotation.surf2_fbn1.gtf.gz").getPath());

    @Nested
    public class SmallGtfFile {

        @Test
        public void iterate() {
            GencodeParser parser = new GencodeParser(SMALL_GTF_PATH, GENOMIC_ASSEMBLY);

            List<GencodeGene> genes = new ArrayList<>();
            for (GencodeGene gene : parser) {
                genes.add(gene);
            }

            assertThat(genes, hasSize(3));
        }

        @Test
        public void stream() {
            GencodeParser parser = new GencodeParser(SMALL_GTF_PATH, GENOMIC_ASSEMBLY);

            List<GencodeGene> genes = parser.stream().sequential()
                    .collect(Collectors.toUnmodifiableList());

            assertThat(genes, hasSize(3));

            // ----------------------------------------- SURF2 ---------------------------------------------------------
            GencodeGene surf2 = genes.get(0);

            // Location stuff
            assertThat(surf2.contigName(), equalTo("9"));
            assertThat(surf2.strand(), equalTo(Strand.POSITIVE));
            assertThat(surf2.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
            assertThat(surf2.start(), equalTo(133_356_550));
            assertThat(surf2.end(), equalTo(133_361_158));

            // ID stuff
            assertThat(surf2.accession(), equalTo("ENSG00000148291.10"));
            assertThat(surf2.symbol(), equalTo("SURF2"));
            assertThat(surf2.id().hgncId().isPresent(), equalTo(true));
            assertThat(surf2.id().hgncId().get(), equalTo("HGNC:11475"));
            assertThat(surf2.evidenceLevel(), equalTo(EvidenceLevel.MANUALLY_ANNOTATED));

            assertThat(surf2.biotype(), equalTo(Biotype.protein_coding));
            assertThat(surf2.tags(), empty());

            // ----------------------------------------- FBN1 ----------------------------------------------------------
            GencodeGene fbn1 = genes.get(1);

            // Location stuff
            assertThat(fbn1.contigName(), equalTo("15"));
            assertThat(fbn1.strand(), equalTo(Strand.NEGATIVE));
            assertThat(fbn1.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
            assertThat(fbn1.start(), equalTo(53_345_469));
            assertThat(fbn1.end(), equalTo(53_582_877));

            // ID stuff
            assertThat(fbn1.accession(), equalTo("ENSG00000166147.15"));
            assertThat(fbn1.symbol(), equalTo("FBN1"));
            assertThat(fbn1.id().hgncId().isPresent(), equalTo(true));
            assertThat(fbn1.id().hgncId().get(), equalTo("HGNC:3603"));
            assertThat(fbn1.evidenceLevel(), equalTo(EvidenceLevel.VERIFIED));

            assertThat(fbn1.biotype(), equalTo(Biotype.protein_coding));
            assertThat(fbn1.tags(), hasItems("overlapping_locus"));

            Map<String, Coordinates> cdsCoordinates = fbn1.codingTranscripts()
                    .collect(Collectors.toMap(Identified::accession, Coding::cdsCoordinates));
            assertThat(cdsCoordinates, hasEntry("ENST00000560355.1", Coordinates.of(CoordinateSystem.oneBased(), 53_346_421, 53_346_588)));
            assertThat(cdsCoordinates, hasEntry("ENST00000316623.10", Coordinates.of(CoordinateSystem.oneBased(), 53_346_421, 53_580_200)));

            // ----------------------------------------- SURF2 on scaffold ----------------------------------------------
            GencodeGene surf2OnScaffold = genes.get(2);
            // Location stuff
            assertThat(surf2OnScaffold.contigName(), equalTo("HG2030_PATCH"));
            assertThat(surf2OnScaffold.strand(), equalTo(Strand.POSITIVE));
            assertThat(surf2OnScaffold.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
            assertThat(surf2OnScaffold.start(), equalTo(182_644));
            assertThat(surf2OnScaffold.end(), equalTo(187_252));

            // ID stuff
            assertThat(surf2OnScaffold.accession(), equalTo("ENSG00000281024.3"));
            assertThat(surf2OnScaffold.symbol(), equalTo("SURF2"));
            assertThat(surf2OnScaffold.id().hgncId().isPresent(), equalTo(true));
            assertThat(surf2OnScaffold.id().hgncId().get(), equalTo("HGNC:11475"));
            assertThat(surf2OnScaffold.evidenceLevel(), equalTo(EvidenceLevel.MANUALLY_ANNOTATED));

            assertThat(surf2OnScaffold.biotype(), equalTo(Biotype.protein_coding));
            assertThat(surf2OnScaffold.tags(), empty());
        }
    }


}