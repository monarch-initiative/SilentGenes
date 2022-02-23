package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.io.gtf.GtfRecord;
import org.monarchinitiative.sgenes.gtf.model.*;
import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqGeneIdentifier;
import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqGeneImpl;
import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqMetadataImpl;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RefseqGeneIterator extends BaseGeneIterator<RefseqGene, RefseqMetadata, RefseqTranscript> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefseqGeneIterator.class);
    public static final Set<String> MANDATORY_TRANSCRIPT_ATTRIBUTES = Set.of();
    public static final Set<String> MANDATORY_EXON_ATTRIBUTES = Set.of();
    private static final Pattern GENE_ID_PT = Pattern.compile("GeneID:(?<payload>\\d+)");
    private static final Pattern HGNC_ID_PT = Pattern.compile("HGNC:HGNC:(?<payload>\\d+)");


    RefseqGeneIterator(Path gencodeGtfPath, GenomicAssembly genomicAssembly) {
        super(gencodeGtfPath,
                genomicAssembly,
                MANDATORY_TRANSCRIPT_ATTRIBUTES,
                MANDATORY_EXON_ATTRIBUTES);
    }

    @Override
    protected Optional<GeneIdentifier> parseGeneIdentifier(String geneId, GtfRecord gene) {
        String hgncId = null;
        String ncbiGeneId = null;
        for (String xRef : gene.attribute("db_xref")) {
            if (hgncId != null && ncbiGeneId != null)
                // we have all IDs
                break;

            // NCBIGene ID
            Matcher geneIdMatcher = GENE_ID_PT.matcher(xRef);
            if (geneIdMatcher.matches()) {
                ncbiGeneId = "NCBIGene:" + geneIdMatcher.group("payload");
                continue;
            }

            //
            Matcher hgncIdMatcher = HGNC_ID_PT.matcher(xRef);
            if (hgncIdMatcher.matches()) {
                hgncId = "HGNC:" + hgncIdMatcher.group("payload");
            }
        }

        if (ncbiGeneId == null) {
            LOGGER.warn("Could not find NCBI Gene ID (GeneId:\\d+) for gene {} in record `{}`", geneId, gene);
            return Optional.empty();
        }

        // In RefSeq GTF, HGNC gene symbol is used in `gene_id` attribute
        return Optional.of(RefseqGeneIdentifier.of(ncbiGeneId, geneId, hgncId));
    }

    @Override
    protected Optional<RefseqMetadata> parseGeneMetadata(String geneId, GtfRecord gene) {
        String biotypeString = gene.firstAttribute("gene_biotype");
        Optional<Biotype> biotype = parseBiotype(biotypeString);
        if (biotype.isEmpty()) {
            LOGGER.warn("Unable to parse biotype level `{}` for gene `{}`", biotypeString, geneId);
            return Optional.empty();
        }
        return Optional.of(RefseqMetadataImpl.of(biotype.get()));
    }

    @Override
    protected Optional<RefseqTranscript> processTranscript(String txId,
                                                     GtfRecord tx,
                                                     List<GtfRecord> exonRecords,
                                                     GtfRecord startCodon,
                                                     GtfRecord stopCodon) {
        Optional<String> txSymbol = getTxSymbol(tx);

        if (txSymbol.isEmpty()) {
            LOGGER.warn("No symbol for transcript `{}` : `{}`", txId, tx);
            return Optional.empty();
        }

        TranscriptIdentifier txIdentifier = TranscriptIdentifier.of(txId, txSymbol.get(), null);

        Optional<RefseqMetadata> metadata = parseTranscriptMetadata(tx);
        if (metadata.isEmpty())
            return Optional.empty();

        // exons
        List<Coordinates> exons = processExons(exonRecords);

        // CDS coordinates
        Optional<Coordinates> cdsCoordinates = createCdsCoordinates(startCodon, stopCodon, txId, tx);

        return Optional.of(
                RefseqTranscript.of(txIdentifier,
                        tx.location(),
                        exons,
                        metadata.get(),
                        cdsCoordinates.orElse(null))
        );
    }

    private static Optional<String> getTxSymbol(GtfRecord tx) {
        String txSymbol = tx.firstAttribute("product");

        if (txSymbol == null) // try `standard_name`
            txSymbol = tx.firstAttribute("standard_name");

        if (txSymbol == null) // fallback to `transcript_id`
            txSymbol = tx.firstAttribute("transcript_id");

        return Optional.ofNullable(txSymbol);
    }

    private static Optional<RefseqMetadata> parseTranscriptMetadata(GtfRecord tx) {
        return parseBiotype(tx.firstAttribute("transcript_biotype"))
                .map(RefseqMetadataImpl::of);
    }

    @Override
    protected RefseqGene newGeneInstance(GeneIdentifier geneIdentifier,
                                         GenomicRegion location,
                                         List<RefseqTranscript> transcripts,
                                         RefseqMetadata refseqMetadata) {
        return RefseqGeneImpl.of(geneIdentifier,
                location,
                transcripts,
                refseqMetadata);
    }

}
