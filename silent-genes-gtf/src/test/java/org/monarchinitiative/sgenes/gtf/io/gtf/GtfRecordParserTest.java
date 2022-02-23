package org.monarchinitiative.sgenes.gtf.io.gtf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.monarchinitiative.svart.CoordinateSystem;
import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.monarchinitiative.svart.Strand;

import java.util.*;

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
        assertThat(record.firstAttribute("gene_type"), equalTo("miRNA"));
        assertThat(record.firstAttribute("gene_name"), equalTo("MIR6859-1"));
        assertThat(record.firstAttribute("hgnc_id"), equalTo("HGNC:50039"));
        assertThat(record.tags(), empty());

        assertThat(record.contigName(), equalTo("1"));
        assertThat(record.strand(), equalTo(Strand.NEGATIVE));
        assertThat(record.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
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
        assertThat(record.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
        assertThat(record.start(), equalTo(12_010));
        assertThat(record.end(), equalTo(13_670));

        assertThat(record.firstAttribute("transcript_type"), equalTo("transcribed_unprocessed_pseudogene"));
        assertThat(record.firstAttribute("transcript_name"), equalTo("DDX11L1-201"));
        assertThat(record.firstAttribute("level"), equalTo("2"));
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
        assertThat(record.coordinateSystem(), equalTo(CoordinateSystem.oneBased()));
        assertThat(record.start(), equalTo(91_255_838));
        assertThat(record.end(), equalTo(91_255_964));

        assertThat(record.firstAttribute("transcript_id"), equalTo("ENST00000368184.8"));
        assertThat(record.firstAttribute("exon_number"), equalTo("257"));
    }

    @ParameterizedTest
    @CsvSource({
            // complicated attribute field in RefSeq GTF
            "gene_id \"DDX11L1\"; transcript_id \"\"; description \"DEAD/H-box helicase 11 like 1 (pseudogene)\"; gene \"DDX11L1\"; gene_biotype \"transcribed_pseudogene\"; pseudo \"true\";, gene_id=DDX11L1;transcript_id=;description=DEAD/H-box helicase 11 like 1 (pseudogene);gene=DDX11L1;gene_biotype=transcribed_pseudogene;pseudo=true",
            // gene attribute field of Gencode GTF
            "gene_id \"ENSG00000223972.5\"; gene_type \"transcribed_unprocessed_pseudogene\"; gene_name \"DDX11L1\"; level 2; hgnc_id \"HGNC:37102\"; havana_gene \"OTTHUMG00000000961.2\";, gene_id=ENSG00000223972.5;gene_type=transcribed_unprocessed_pseudogene;gene_name=DDX11L1;level=2;hgnc_id=HGNC:37102;havana_gene=OTTHUMG00000000961.2",
            // another Refseq gene, repeated attribute tags
            "gene_id \"SURF1\"; transcript_id \"\"; db_xref \"GeneID:6834\"; db_xref \"HGNC:HGNC:11474\"; db_xref \"MIM:185620\"; description \"SURF1 cytochrome c oxidase assembly factor\"; gbkey \"Gene\"; gene \"SURF1\"; gene_biotype \"protein_coding\"; gene_synonym \"CMT4K\"; gene_synonym \"MC4DN1\"; gene_synonym \"SHY1\";, gene_id=SURF1;transcript_id=;db_xref=GeneID:6834;db_xref=HGNC:HGNC:11474;db_xref=MIM:185620;description=SURF1 cytochrome c oxidase assembly factor;gbkey=Gene;gene=SURF1;gene_biotype=protein_coding;gene_synonym=CMT4K;gene_synonym=MC4DN1;gene_synonym=SHY1",
            // transcript attributes
            "gene_id \"FBN1\"; transcript_id \"NM_000138.5\"; db_xref \"Ensembl:ENST00000316623.10\"; db_xref \"GeneID:2200\"; gbkey \"mRNA\"; gene \"FBN1\"; product \"fibrillin 1\"; tag \"MANE Select\"; transcript_biotype \"mRNA\";, gene_id=FBN1;transcript_id=NM_000138.5;db_xref=Ensembl:ENST00000316623.10;db_xref=GeneID:2200;gbkey=mRNA;gene=FBN1;product=fibrillin 1;tag=MANE Select;transcript_biotype=mRNA",
            // exon attributes with some special characters in the product attributes
            "gene_id \"ATP1B1\"; transcript_id \"NM_001677.4\"; db_xref \"GeneID:481\"; gbkey \"mRNA\"; gene \"ATP1B1\"; product \"ATPase Na+/K+ transporting subunit beta 1\"; tag \"RefSeq Select\"; exon_number \"1\";, gene_id=ATP1B1;transcript_id=NM_001677.4;db_xref=GeneID:481;gbkey=mRNA;gene=ATP1B1;product=ATPase Na+/K+ transporting subunit beta 1;tag=RefSeq Select;exon_number=1",
            "product \"RNA 3'-terminal phosphate cyclase transcript variant 1\"; exon_number \"1\";, product=RNA 3'-terminal phosphate cyclase transcript variant 1;exon_number=1",
            "gene \"HLA-A\"; product \"major histocompatibility complex class I A transcript variant 1 (A*03:01:01:01)\";, gene=HLA-A;product=major histocompatibility complex class I A transcript variant 1 (A*03:01:01:01)"
    })
    public void parseAttributes(String payload, String expected) throws Exception {
        Map<String, List<String>> expectedMap = prepareExpectedAttributes(expected);
        Map<String, List<String>> actual = GtfRecordParser.parseAttributes(payload);

        assertThat(actual, equalTo(expectedMap));
    }

    private static Map<String, List<String>> prepareExpectedAttributes(String expected) {
        Map<String, List<String>> attributes = new HashMap<>();

        String[] tokens = expected.split(";");
        for (String token : tokens) {
            String[] subs = token.split("=", 2);
            attributes.computeIfAbsent(subs[0], k -> new LinkedList<>()).add(subs[1]);
        }

        return attributes;
    }
}