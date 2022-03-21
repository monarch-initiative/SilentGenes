package org.monarchinitiative.sgenes.jannovar;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.Strand;
import org.monarchinitiative.sgenes.model.Gene;
import org.monarchinitiative.sgenes.model.Identified;
import org.monarchinitiative.sgenes.model.Transcript;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JannovarParserTest {

    /**
     * Folder with small Jannovar cache files that contain transcripts of the following genes:
     * <ul>
     *     <li><em>SURF1</em></li>
     *     <li><em>SURF2</em></li>
     *     <li><em>FBN1</em></li>
     *     <li><em>ZNF436</em></li>
     *     <li><em>ZBTB48</em></li>
     *     <li><em>HNF4A</em></li>
     *     <li><em>GCK</em></li>
     *     <li><em>BRCA2</em></li>
     *     <li><em>MEIOB</em></li>
     *     <li><em>FAHD1</em></li>
     *     <li><em>COL4A5</em></li> (on <code>chrX</code>)
     *     <li><em>SRY</em></li> (on <code>chrY</code>)
     * </ul>
     */
    private static final Path BASE_DIR = Path.of("src/test/resources/org/monarchinitiative/sgenes/jannovar");
    private static final String[] EXPECTED_SYMBOLS = {"SURF1", "SURF2", "FBN1", "ZNF436", "ZBTB48", "HNF4A", "GCK",
            "BRCA2", "MEIOB", "FAHD1", "COL4A5", "SRY"};

    @Nested
    public class Hg19Tests {

        private final Path HG19_PATH = BASE_DIR.resolve("hg19");

        private final Path REFSEQ = HG19_PATH.resolve("hg19_refseq.small.ser");
        private final Path ENSEMBL = HG19_PATH.resolve("hg19_ensembl.small.ser");
        private final Path UCSC = HG19_PATH.resolve("hg19_ucsc.small.ser");

        @Test
        public void parseRefseq() {
            JannovarParser parser = JannovarParser.of(REFSEQ, GenomicAssemblies.GRCh37p13());
            Map<String, Gene> genesBySymbol = parser.stream()
                    .collect(Collectors.toUnmodifiableMap(Gene::symbol, Function.identity()));

            assertThat(genesBySymbol.keySet(), hasSize(12));
            assertThat(genesBySymbol.keySet(), hasItems(EXPECTED_SYMBOLS));

            Gene surf1 = genesBySymbol.get("SURF1");
            assertThat(surf1.contigName(), equalTo("9"));
            assertThat(surf1.strand(), equalTo(Strand.NEGATIVE));
            assertThat(surf1.coordinateSystem(), equalTo(CoordinateSystem.zeroBased()));
            assertThat(surf1.start(), equalTo(4_990_070));
            assertThat(surf1.end(), equalTo(4_994_772));

            assertThat(surf1.accession(), equalTo("ENSG00000148290"));
            assertThat(surf1.symbol(), equalTo("SURF1"));
            assertThat(surf1.id().hgncId().isPresent(), equalTo(true));
            assertThat(surf1.id().hgncId().get(), equalTo("HGNC:11474"));
            assertThat(surf1.id().ncbiGeneId().isPresent(), equalTo(true));
            assertThat(surf1.id().ncbiGeneId().get(), equalTo("NCBIGene:6834"));

            List<Transcript> transcripts = surf1.transcriptStream()
                    .collect(Collectors.toUnmodifiableList());

            assertThat(transcripts, hasSize(2));
            Set<String> accessions = transcripts.stream()
                    .map(Identified::accession)
                    .collect(Collectors.toUnmodifiableSet());
            assertThat(accessions, hasItems("NM_003172.3", "NM_001280787.1"));
        }

        @Test
        public void parseEnsembl() {
            JannovarParser parser = JannovarParser.of(ENSEMBL, GenomicAssemblies.GRCh37p13());
            Map<String, Gene> genesBySymbols = parser.stream()
                    .collect(Collectors.toUnmodifiableMap(Gene::symbol, Function.identity()));

            assertThat(genesBySymbols.keySet(), hasSize(12));
            assertThat(genesBySymbols.keySet(), hasItems(EXPECTED_SYMBOLS));
        }

        @Test
        public void parseUcsc() {
            JannovarParser parser = JannovarParser.of(UCSC, GenomicAssemblies.GRCh37p13());
            Map<String, Gene> genesBySymbols = parser.stream()
                    .collect(Collectors.toUnmodifiableMap(Gene::symbol, Function.identity()));

            assertThat(genesBySymbols.keySet(), hasSize(12));
            assertThat(genesBySymbols.keySet(), hasItems(EXPECTED_SYMBOLS));
        }

    }

    @Nested
    public class Hg38Tests {

        private final Path HG38_PATH = BASE_DIR.resolve("hg38");
        private final Path REFSEQ = HG38_PATH.resolve("hg38_refseq.small.ser");
        private final Path ENSEMBL = HG38_PATH.resolve("hg38_ensembl.small.ser");
        private final Path UCSC = HG38_PATH.resolve("hg38_ucsc.small.ser");

        @Test
        public void parseRefseq() {
            JannovarParser parser = JannovarParser.of(REFSEQ, GenomicAssemblies.GRCh38p13());
            Map<String, Gene> genesBySymbol = parser.stream()
                    .collect(Collectors.toUnmodifiableMap(Gene::symbol, Function.identity()));

            assertThat(genesBySymbol.keySet(), hasSize(12));
            assertThat(genesBySymbol.keySet(), hasItems(EXPECTED_SYMBOLS));

            Gene surf1 = genesBySymbol.get("SURF1");
            assertThat(surf1.contigName(), equalTo("9"));
            assertThat(surf1.strand(), equalTo(Strand.NEGATIVE));
            assertThat(surf1.coordinateSystem(), equalTo(CoordinateSystem.zeroBased()));
            assertThat(surf1.start(), equalTo(5_038_232));
            assertThat(surf1.end(), equalTo(5_042_913));

            assertThat(surf1.accession(), equalTo("ENSG00000148290"));
            assertThat(surf1.symbol(), equalTo("SURF1"));
            GeneIdentifier surf1Id = surf1.id();
            assertThat(surf1Id.hgncId().isPresent(), equalTo(true));
            assertThat(surf1Id.hgncId().get(), equalTo("HGNC:11474"));
            assertThat(surf1Id.ncbiGeneId().isPresent(), equalTo(true));
            assertThat(surf1Id.ncbiGeneId().get(), equalTo("NCBIGene:6834"));

            List<Transcript> transcripts = surf1.transcriptStream()
                    .collect(Collectors.toUnmodifiableList());

            assertThat(transcripts, hasSize(3));
            Set<String> accessions = transcripts.stream().map(Identified::accession).collect(Collectors.toUnmodifiableSet());
            assertThat(accessions, hasItems("NM_003172.3", "NM_001280787.1", "XM_011518942.1"));
        }

        @Test
        public void parseEnsembl() {
            JannovarParser parser = JannovarParser.of(ENSEMBL, GenomicAssemblies.GRCh38p13());
            Map<String, Gene> genesBySymbol = parser.stream()
                    .collect(Collectors.toUnmodifiableMap(Gene::symbol, Function.identity()));

            assertThat(genesBySymbol.keySet(), hasSize(12));
            assertThat(genesBySymbol.keySet(), hasItems(EXPECTED_SYMBOLS));
        }

        @Test
        public void parseUcsc() {
            JannovarParser parser = JannovarParser.of(UCSC, GenomicAssemblies.GRCh38p13());
            Map<String, Gene> genesBySymbol = parser.stream()
                    .collect(Collectors.toUnmodifiableMap(Gene::symbol, Function.identity()));

            assertThat(genesBySymbol.keySet(), hasSize(12));
            assertThat(genesBySymbol.keySet(), hasItems(EXPECTED_SYMBOLS));
        }
    }

}