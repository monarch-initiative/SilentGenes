package org.monarchinitiative.sgenes.gtf.io.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.sgenes.gtf.io.GtfGeneParser;
import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.sgenes.model.Coding;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.sgenes.model.Identified;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.Strand;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RefseqParserTest {

    private static final GenomicAssembly GENOMIC_ASSEMBLY = GenomicAssemblies.GRCh38p13();

    /**
     * A GTF file with definitions of SURF2 and FBN1 genes from RefSeq.
     * The file was prepared by running:
     * <pre>
     * zcat GCF_000001405.39_GRCh38.p13_genomic.gtf.gz | grep "^#" > GCF_000001405.39_GRCh38.p13_genomic.surf2_fbn1.gtf
     * zcat GCF_000001405.39_GRCh38.p13_genomic.gtf.gz | grep "SURF2" >> GCF_000001405.39_GRCh38.p13_genomic.surf2_fbn1.gtf
     * zcat GCF_000001405.39_GRCh38.p13_genomic.gtf.gz | grep "FBN1" >> GCF_000001405.39_GRCh38.p13_genomic.surf2_fbn1.gtf
     * gzip GCF_000001405.39_GRCh38.p13_genomic.surf2_fbn1.gtf
     * </pre>
     * Then, we removed <em>FBN-DT</em> and the <em>SURF2_1</em>.
     */
    private static final Path SMALL_GTF_PATH = Path.of("src/test/resources/org/monarchinitiative/sgenes/gtf/io/impl/GCF_000001405.39_GRCh38.p13_genomic.surf2_fbn1.gtf.gz");

    @Nested
    public class SmallGtfFileTest {

        @Test
        public void iterate() {
            RefseqParser parser = new RefseqParser(SMALL_GTF_PATH, GENOMIC_ASSEMBLY);

            List<RefseqGene> genes = new ArrayList<>();
            for (RefseqGene gene : parser) {
                genes.add(gene);
            }

            assertThat(genes, hasSize(2));
        }

        @Test
        public void stream() {

            RefseqParser parser = new RefseqParser(SMALL_GTF_PATH, GENOMIC_ASSEMBLY);

            List<RefseqGene> genes = parser.stream().sequential()
                    .collect(Collectors.toUnmodifiableList());

            assertThat(genes, hasSize(2));

            // ----------------------------------------- SURF2 ---------------------------------------------------------
            RefseqGene surf2 = genes.get(0);

            // Location stuff
            assertThat(surf2.contigName(), equalTo("9"));
            assertThat(surf2.strand(), equalTo(Strand.POSITIVE));
            assertThat(surf2.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
            assertThat(surf2.start(), equalTo(133_356_550));
            assertThat(surf2.end(), equalTo(133_361_158));

            // ID stuff
            assertThat(surf2.accession(), equalTo("NCBIGene:6835"));
            assertThat(surf2.symbol(), equalTo("SURF2"));
            assertThat(surf2.id().hgncId().isPresent(), equalTo(true));
            assertThat(surf2.id().hgncId().get(), equalTo("HGNC:11475"));
            assertThat(surf2.id().ncbiGeneId().isPresent(), equalTo(true));
            assertThat(surf2.id().ncbiGeneId().get(), equalTo("NCBIGene:6835"));

            // Metadata
            assertThat(surf2.refseqMetadata().biotype(), equalTo(Biotype.protein_coding));

            // Transcripts
            assertThat(surf2.transcriptCount(), equalTo(2));

            // ----------------------------------------- FBN1 ----------------------------------------------------------
            RefseqGene fbn1 = genes.get(1);

            // Location stuff
            assertThat(fbn1.contigName(), equalTo("15"));
            assertThat(fbn1.strand(), equalTo(Strand.NEGATIVE));
            assertThat(fbn1.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
            assertThat(fbn1.start(), equalTo(53_345_481));
            assertThat(fbn1.end(), equalTo(53_582_877));

            // ID stuff
            assertThat(fbn1.accession(), equalTo("NCBIGene:2200"));
            assertThat(fbn1.symbol(), equalTo("FBN1"));
            assertThat(fbn1.id().hgncId().isPresent(), equalTo(true));
            assertThat(fbn1.id().hgncId().get(), equalTo("HGNC:3603"));
            assertThat(fbn1.id().ncbiGeneId().isPresent(), equalTo(true));
            assertThat(fbn1.id().ncbiGeneId().get(), equalTo("NCBIGene:2200"));

            // Metadata
            assertThat(fbn1.refseqMetadata().biotype(), equalTo(Biotype.protein_coding));

            // Transcripts
            assertThat(fbn1.transcriptCount(), equalTo(1));

            Map<String, Coordinates> cdsCoordinates = fbn1.codingTranscripts()
                    .collect(Collectors.toMap(Identified::accession, Coding::cdsCoordinates));
            assertThat(cdsCoordinates, hasEntry("NM_000138.5", Coordinates.of(CoordinateSystem.oneBased(), 53_346_421, 53_580_200)));

        }
    }

}