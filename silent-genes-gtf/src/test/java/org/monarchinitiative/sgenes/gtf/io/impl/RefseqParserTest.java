package org.monarchinitiative.sgenes.gtf.io.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.RefseqGene;
import org.monarchinitiative.sgenes.gtf.model.RefseqSource;
import org.monarchinitiative.sgenes.gtf.model.RefseqTranscript;
import org.monarchinitiative.sgenes.model.*;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.svart.Strand;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RefseqParserTest {

    private static final Path IMPL_DIR = Path.of("src/test/resources/org/monarchinitiative/sgenes/gtf/io/impl");

    @Nested
    public class SmallGtfFileTest {
        private final GenomicAssembly GRCH38 = GenomicAssemblies.GRCh38p13();
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
        private final Path GTF_PATH = IMPL_DIR.resolve("GCF_000001405.39_GRCh38.p13_genomic.surf2_fbn1.gtf.gz");

        @Test
        public void iterate() {
            RefseqParser parser = new RefseqParser(GTF_PATH, GRCH38);

            List<RefseqGene> genes = new ArrayList<>();
            for (RefseqGene gene : parser) {
                genes.add(gene);
            }

            assertThat(genes, hasSize(2));
        }

        @Test
        public void stream() {

            RefseqParser parser = new RefseqParser(GTF_PATH, GRCH38);

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
            assertThat(surf2.biotype(), equalTo(Biotype.protein_coding));

            // Transcripts
            assertThat(surf2.transcriptCount(), equalTo(2));
            Iterator<? extends RefseqTranscript> surf2Iterator = surf2.transcripts();
            RefseqTranscript surf2Tx = surf2Iterator.next();
            assertThat(surf2Tx.evidence().isPresent(), equalTo(true));
            assertThat(surf2Tx.evidence().get(), equalTo(TranscriptEvidence.KNOWN));
            assertThat(surf2Tx.featureSource().isPresent(), equalTo(true));
            assertThat(surf2Tx.featureSource().get(), equalTo(FeatureSource.REFSEQ));

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
            assertThat(fbn1.biotype(), equalTo(Biotype.protein_coding));

            // Transcripts
            assertThat(fbn1.transcriptCount(), equalTo(1));
            Iterator<? extends RefseqTranscript> fbn1Iterator = fbn1.transcripts();
            RefseqTranscript fbn1Tx = fbn1Iterator.next();
            assertThat(fbn1Tx.evidence().isPresent(), equalTo(true));
            assertThat(fbn1Tx.evidence().get(), equalTo(TranscriptEvidence.KNOWN));
            assertThat(fbn1Tx.featureSource().isPresent(), equalTo(true));
            assertThat(fbn1Tx.featureSource().get(), equalTo(FeatureSource.REFSEQ));

            Map<String, Coordinates> cdsCoordinates = fbn1.codingTranscripts()
                    .collect(Collectors.toMap(Identified::accession, Coding::cdsCoordinates));
            assertThat(cdsCoordinates, hasEntry("NM_000138.5", Coordinates.of(CoordinateSystem.oneBased(), 53_346_421, 53_580_200)));

        }
    }

    /**
     * Older (hg19) RefSeq releases do not include `transcript` rows. We should infer the transcript data from other rows.
     */
    @Nested
    public class MissingTranscriptRecordsTest {

        private final GenomicAssembly GRCH37 = GenomicAssemblies.GRCh37p13();
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
        private final Path GTF_PATH = IMPL_DIR.resolve("GCF_000001405.25_GRCh37.p13_genomic.surf2.gtf.gz");

        @Test
        public void iterate() {
            RefseqParser parser = new RefseqParser(GTF_PATH, GRCH37);

            List<RefseqGene> genes = new ArrayList<>();
            for (RefseqGene gene : parser) {
                genes.add(gene);
            }
            assertThat(genes, hasSize(1));


            RefseqGene surf2 = genes.get(0);

            // Location stuff
            assertThat(surf2.contigName(), equalTo("9"));
            assertThat(surf2.strand(), equalTo(Strand.POSITIVE));
            assertThat(surf2.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
            assertThat(surf2.start(), equalTo(136_223_426));
            assertThat(surf2.end(), equalTo(136_228_034));

            // ID stuff
            assertThat(surf2.accession(), equalTo("NCBIGene:6835"));
            assertThat(surf2.symbol(), equalTo("SURF2"));
            assertThat(surf2.id().hgncId().isPresent(), equalTo(true));
            assertThat(surf2.id().hgncId().get(), equalTo("HGNC:11475"));
            assertThat(surf2.id().ncbiGeneId().isPresent(), equalTo(true));
            assertThat(surf2.id().ncbiGeneId().get(), equalTo("NCBIGene:6835"));

            // Metadata
            assertThat(surf2.biotype(), equalTo(Biotype.protein_coding));
            assertThat(surf2.featureSource().isPresent(), equalTo(true));
            assertThat(surf2.featureSource().get(), equalTo(FeatureSource.REFSEQ));

            // Transcripts
            assertThat(surf2.transcriptCount(), equalTo(2));
            List<? extends RefseqTranscript> txs = surf2.transcriptStream()
                    .sorted(Comparator.comparing(Identified::accession))
                    .collect(Collectors.toList());

            // - NM_001278928.2
            RefseqTranscript first = txs.get(0);
            assertThat(first.accession(), equalTo("NM_001278928.2"));
            assertThat(first.symbol(), equalTo("surfeit 2, transcript variant 2"));
            assertThat(first.id().ccdsId().isPresent(), equalTo(false));
            assertThat(first.location(), equalTo(GenomicRegion.of(GRCH37.contigByName("9"), Strand.POSITIVE, CoordinateSystem.zeroBased(), 136_223_425, 136_228_034)));
            assertThat(first.exons().size(), equalTo(6));
            assertThat(first.evidence().isPresent(), equalTo(true));
            assertThat(first.evidence().get(), equalTo(TranscriptEvidence.KNOWN));
            assertThat(first.featureSource().isPresent(), equalTo(true));
            assertThat(first.featureSource().get(), equalTo(FeatureSource.REFSEQ));
            assertThat(first.biotype(), equalTo(Biotype.protein_coding));
            assertThat(first.source(), equalTo(RefseqSource.BestRefSeq));

            assertThat(first, instanceOf(CodingTranscript.class));
            CodingTranscript firstCoding = (CodingTranscript) first;
            assertThat(firstCoding.startCodon().startWithCoordinateSystem(CoordinateSystem.zeroBased()), equalTo(136_223_468));
            assertThat(firstCoding.stopCodon().endWithCoordinateSystem(CoordinateSystem.zeroBased()), equalTo(136_228_015));

            // - NM_017503.5
            RefseqTranscript second = txs.get(1);
            assertThat(second.accession(), equalTo("NM_017503.5"));
            assertThat(second.symbol(), equalTo("surfeit 2, transcript variant 1"));
            assertThat(second.id().ccdsId().isPresent(), equalTo(false));
            assertThat(second.location(), equalTo(GenomicRegion.of(GRCH37.contigByName("9"), Strand.POSITIVE, CoordinateSystem.zeroBased(), 136_223_425, 136_228_034)));
            assertThat(second.exons().size(), equalTo(6));
            assertThat(second.evidence().isPresent(), equalTo(true));
            assertThat(second.evidence().get(), equalTo(TranscriptEvidence.KNOWN));
            assertThat(second.featureSource().isPresent(), equalTo(true));
            assertThat(second.featureSource().get(), equalTo(FeatureSource.REFSEQ));
            assertThat(second.biotype(), equalTo(Biotype.protein_coding));
            assertThat(second.source(), equalTo(RefseqSource.BestRefSeq));

            assertThat(second, instanceOf(CodingTranscript.class));
            CodingTranscript secondCoding = (CodingTranscript) second;
            assertThat(secondCoding.startCodon().startWithCoordinateSystem(CoordinateSystem.zeroBased()), equalTo(136_223_468));
            assertThat(secondCoding.stopCodon().endWithCoordinateSystem(CoordinateSystem.zeroBased()), equalTo(136_228_015));
        }
    }

}