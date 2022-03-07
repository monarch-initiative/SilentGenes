package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.io.gtf.GtfRecord;
import org.monarchinitiative.sgenes.gtf.io.gtf.GtfSource;
import org.monarchinitiative.sgenes.gtf.model.*;
import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqGeneIdentifier;
import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqGeneImpl;
import org.monarchinitiative.sgenes.gtf.model.impl.refseq.RefseqTranscriptTranscriptMetadataImpl;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.TranscriptEvidence;
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

class RefseqGeneIterator extends BaseGeneIterator<RefseqGene, Biotype, RefseqTranscript> {

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
    protected Optional<Biotype> parseGeneMetadata(String geneId, GtfRecord gene) {
        String biotypeString = gene.firstAttribute("gene_biotype");
        Optional<Biotype> biotype = parseBiotype(biotypeString);
        if (biotype.isEmpty()) {
            LOGGER.warn("Unable to parse biotype level `{}` for gene `{}`", biotypeString, geneId);
            return Optional.empty();
        }
        return biotype;
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

        Optional<RefseqTranscriptMetadata> metadata = parseTranscriptMetadata(tx);
        if (metadata.isEmpty())
            return Optional.empty();

        // exons
        List<Coordinates> exons = processExons(exonRecords);

        // CDS coordinates
        Optional<Coordinates> cdsCoordinates = createCdsCoordinates(startCodon, stopCodon, txId, tx);

        Optional<RefseqSource> refseqSource = parseRefseqSource(tx.source());
        if (refseqSource.isEmpty()) {
            LOGGER.warn("Unknown RefSeq source {} for transcript {}", tx.source(), txId);
            return Optional.empty();
        }

        return Optional.of(
                RefseqTranscript.of(txIdentifier,
                        tx.location(),
                        exons,
                        refseqSource.get(),
                        metadata.get(),
                        cdsCoordinates.orElse(null)));
    }

    private static Optional<String> getTxSymbol(GtfRecord tx) {
        String txSymbol = tx.firstAttribute("product");

        if (txSymbol == null) // try `standard_name`
            txSymbol = tx.firstAttribute("standard_name");

        if (txSymbol == null) // fallback to `transcript_id`
            txSymbol = tx.firstAttribute("transcript_id");

        return Optional.ofNullable(txSymbol);
    }

    private static Optional<RefseqTranscriptMetadata> parseTranscriptMetadata(GtfRecord tx) {
        Optional<Biotype> biotype = parseBiotype(tx.firstAttribute("transcript_biotype"));

        if (biotype.isPresent()) {
            TranscriptEvidence evidence = parseTranscriptEvidence(tx);
            return Optional.of(RefseqTranscriptTranscriptMetadataImpl.of(evidence, biotype.get()));
        } else {
            return Optional.empty();
        }
    }

    private static TranscriptEvidence parseTranscriptEvidence(GtfRecord tx) {
        String txId = tx.firstAttribute("transcript_id");
        if (txId.startsWith("NM_") || txId.startsWith("NR_"))
            return TranscriptEvidence.KNOWN;
        else if (txId.startsWith("XM_") || txId.startsWith("XR_"))
            return TranscriptEvidence.MODEL;
        LOGGER.warn("Cannot infer TranscriptEvidence value for transcript `{}`", txId);
        return null;
    }

    private static Optional<RefseqSource> parseRefseqSource(GtfSource source) {
        switch (source) {
            case GNOMON:
                return Optional.of(RefseqSource.Gnomon);
            case BEST_REF_SEQ:
                return Optional.of(RefseqSource.BestRefSeq);
            case REF_SEQ:
                return Optional.of(RefseqSource.RefSeq);
            case CURATED_GENOMIC:
                return Optional.of(RefseqSource.CuratedGenomic);
            case T_RNA_SCAN_SE:
                return Optional.of(RefseqSource.tRNAScanSe);
            default:
                return Optional.empty();
        }
    }

    @Override
    protected RefseqGene newGeneInstance(GeneIdentifier geneIdentifier,
                                         GenomicRegion location,
                                         List<RefseqTranscript> transcripts,
                                         Biotype biotype) {
        return RefseqGeneImpl.of(geneIdentifier,
                location,
                transcripts,
                biotype);
    }

}
