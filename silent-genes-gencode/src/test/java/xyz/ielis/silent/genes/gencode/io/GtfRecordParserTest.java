package xyz.ielis.silent.genes.gencode.io;

import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.GenomicAssemblies;
import org.monarchinitiative.svart.GenomicAssembly;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GtfRecordParserTest {

    private static final GenomicAssembly ASSEMBLY = GenomicAssemblies.GRCh38p13();

    @Test
    public void parse_gene() {
        String payload = "chr1\tENSEMBL\tgene\t17369\t17436\t.\t-\t.\tgene_id \"ENSG00000278267.1\"; gene_type \"miRNA\"; gene_name \"MIR6859-1\"; level 3; hgnc_id \"HGNC:50039\";";
        Optional<GtfRecord> recordOptional = GtfRecordParser.parseLine(payload, ASSEMBLY);

        assertThat(recordOptional.isPresent(), equalTo(true));
        GtfRecord record = recordOptional.get();

        assertThat(record.source(), equalTo(GtfSource.ENSEMBL));
        assertThat(record.feature(), equalTo(GtfFeature.GENE));
        assertThat(record.frame(), equalTo(GtfFrame.NA));
        assertThat(record.geneId(), equalTo("ENSG00000278267.1"));
        assertThat(record.attribute("gene_type"), equalTo("miRNA"));
        assertThat(record.attribute("gene_name"), equalTo("MIR6859-1"));
        assertThat(record.attribute("hgnc_id"), equalTo("HGNC:50039"));
        assertThat(record.attribute("gene_name"), equalTo("MIR6859-1"));
    }

    @Test
    public void parse_transcript() {
        String payload = "chr1\tHAVANA\ttranscript\t12010\t13670\t.\t+\t.\tgene_id \"ENSG00000223972.5\"; transcript_id \"ENST00000450305.2\"; gene_type \"transcribed_unprocessed_pseudogene\"; gene_name \"DDX11L1\"; transcript_type \"transcribed_unprocessed_pseudogene\"; transcript_name \"DDX11L1-201\"; level 2; transcript_support_level \"NA\"; hgnc_id \"HGNC:37102\"; ont \"PGO:0000005\"; ont \"PGO:0000019\"; tag \"basic\"; tag \"Ensembl_canonical\"; havana_gene \"OTTHUMG00000000961.2\"; havana_transcript \"OTTHUMT00000002844.2\";";
        Optional<GtfRecord> recordOptional = GtfRecordParser.parseLine(payload, ASSEMBLY);

        assertThat(recordOptional.isPresent(), equalTo(true));
        GtfRecord record = recordOptional.get();
        assertThat(record.source(), equalTo(GtfSource.HAVANA));
        assertThat(record.feature(), equalTo(GtfFeature.TRANSCRIPT));
        assertThat(record.frame(), equalTo(GtfFrame.NA));
        assertThat(record.geneId(), equalTo("ENSG00000223972.5"));
        // TODO: 11/4/21 improve
//        assertThat(record.getAttributes(), hasEntry("gene_type", "miRNA"));
//        assertThat(record.getAttributes(), hasEntry("gene_name", "MIR6859-1"));
//        assertThat(record.getAttributes(), hasEntry("hgnc_id", "HGNC:50039"));
//        assertThat(record.getAttributes(), hasEntry("gene_name", "MIR6859-1"));
    }
}