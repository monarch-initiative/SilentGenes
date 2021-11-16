package xyz.ielis.silent.genes.gencode.io;

import org.junit.jupiter.api.Test;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.GenomicAssemblies;
import org.monarchinitiative.svart.GenomicAssembly;
import org.monarchinitiative.svart.Strand;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        assertThat(record.tags(), empty());

        assertThat(record.contigName(), equalTo("1"));
        assertThat(record.strand(), equalTo(Strand.NEGATIVE));
        assertThat(record.coordinateSystem(), equalTo(CoordinateSystem.FULLY_CLOSED));
        assertThat(record.start(), equalTo(248_938_987));
        assertThat(record.end(), equalTo(248_939_054));
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
        assertThat(record.tags(), hasItems("basic", "Ensembl_canonical"));

        assertThat(record.contigName(), equalTo("1"));
        assertThat(record.strand(), equalTo(Strand.POSITIVE));
        assertThat(record.coordinateSystem(), equalTo(CoordinateSystem.FULLY_CLOSED));
        assertThat(record.start(), equalTo(12_010));
        assertThat(record.end(), equalTo(13_670));

        assertThat(record.attribute("transcript_type"), equalTo("transcribed_unprocessed_pseudogene"));
        assertThat(record.attribute("transcript_name"), equalTo("DDX11L1-201"));
        assertThat(record.attribute("level"), equalTo("2"));
    }

    @Test
    public void parse_exon() {
        String payload = "chr1\tHAVANA\texon\t157700459\t157700585\t.\t-\t.\tgene_id \"ENSG00000160856.21\"; transcript_id \"ENST00000368184.8\"; gene_type \"protein_coding\"; gene_name \"FCRL3\"; transcript_type \"protein_coding\"; transcript_name \"FCRL3-201\"; exon_number 257; exon_id \"ENSE00003522475.1\"; level 2; protein_id \"ENSP00000357167.3\"; transcript_support_level \"1\"; hgnc_id \"HGNC:18506\"; tag \"alternative_3_UTR\"; tag \"basic\"; tag \"Ensembl_canonical\"; tag \"MANE_Select\"; tag \"appris_principal_2\"; tag \"CCDS\"; ccdsid \"CCDS1167.1\"; havana_gene \"OTTHUMG00000019400.9\"; havana_transcript \"OTTHUMT00000051419.3\";";
        Optional<GtfRecord> recordOptional = GtfRecordParser.parseLine(payload, ASSEMBLY);

        assertThat(recordOptional.isPresent(), equalTo(true));
        GtfRecord record = recordOptional.get();

        assertThat(record.source(), equalTo(GtfSource.HAVANA));
        assertThat(record.feature(), equalTo(GtfFeature.EXON));
        assertThat(record.frame(), equalTo(GtfFrame.NA));
        assertThat(record.geneId(), equalTo("ENSG00000160856.21"));

        assertThat(record.contigName(), equalTo("1"));
        assertThat(record.strand(), equalTo(Strand.NEGATIVE));
        assertThat(record.coordinateSystem(), equalTo(CoordinateSystem.FULLY_CLOSED));
        assertThat(record.start(), equalTo(91_255_838));
        assertThat(record.end(), equalTo(91_255_964));

        assertThat(record.attribute("transcript_id"), equalTo("ENST00000368184.8"));
        assertThat(record.attribute("exon_number"), equalTo("257"));
    }
}