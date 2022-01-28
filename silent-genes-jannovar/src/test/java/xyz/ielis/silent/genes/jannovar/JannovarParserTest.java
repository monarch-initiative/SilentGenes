package xyz.ielis.silent.genes.jannovar;

import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.GenomicAssemblies;
import org.monarchinitiative.svart.Strand;
import xyz.ielis.silent.genes.model.Gene;
import xyz.ielis.silent.genes.model.Identified;
import xyz.ielis.silent.genes.model.Transcript;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JannovarParserTest {

    /**
     * Small Jannovar cache that contains RefSeq transcripts of the following genes:
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
    private static final Path JANNOVAR_CACHE_PATH = Path.of("src/test/resources/xyz/ielis/silent/genes/jannovar/hg38_refseq_small.ser");

    @Test
    public void parse() {
        JannovarParser parser = JannovarParser.of(JANNOVAR_CACHE_PATH, GenomicAssemblies.GRCh38p13());
        Map<String, Gene> genes = parser.stream().collect(Collectors.toUnmodifiableMap(Gene::symbol, Function.identity()));

        assertThat(genes.keySet(), hasSize(12));
        String[] expectedSymbols = {"SURF1", "SURF2", "FBN1", "ZNF436", "ZBTB48", "HNF4A", "GCK", "BRCA2", "MEIOB", "FAHD1", "COL4A5", "SRY"};
        Set<String> actualSymbols = genes.values().stream().map(Gene::symbol).collect(Collectors.toUnmodifiableSet());
        assertThat(actualSymbols, hasItems(expectedSymbols));

        Gene surf1 = genes.get("SURF1");
        assertThat(surf1.contigName(), equalTo("9"));
        assertThat(surf1.strand(), equalTo(Strand.NEGATIVE));
        assertThat(surf1.coordinateSystem(), equalTo(CoordinateSystem.zeroBased()));
        assertThat(surf1.start(), equalTo(5_038_232));
        assertThat(surf1.end(), equalTo(5_042_913));

        assertThat(surf1.accession(), equalTo("ENSG00000148290"));
        assertThat(surf1.symbol(), equalTo("SURF1"));
        assertThat(surf1.id().hgncId().isPresent(), equalTo(true));
        assertThat(surf1.id().hgncId().get(), equalTo("HGNC:11474"));
        assertThat(surf1.id().ncbiGeneId().isPresent(), equalTo(false));

        List<Transcript> transcripts = new ArrayList<>();
        surf1.transcriptStream().forEach(transcripts::add);

        assertThat(transcripts, hasSize(3));
        Set<String> accessions = transcripts.stream().map(Identified::accession).collect(Collectors.toUnmodifiableSet());
        assertThat(accessions, hasItems("NM_003172.3", "NM_001280787.1", "XM_011518942.1"));
    }
}