package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.model.Biotype;
import org.monarchinitiative.sgenes.gtf.model.EvidenceLevel;
import org.monarchinitiative.sgenes.gtf.model.GencodeGene;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscriptMetadata;
import org.monarchinitiative.sgenes.gtf.model.GencodeTranscript;
import org.monarchinitiative.sgenes.gtf.model.impl.gencode.GencodeGeneImpl;
import org.monarchinitiative.sgenes.gtf.io.gtf.GtfRecord;
import org.monarchinitiative.sgenes.model.TranscriptEvidence;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.svart.assembly.GenomicAssembly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.monarchinitiative.sgenes.model.GeneIdentifier;
import org.monarchinitiative.sgenes.model.TranscriptIdentifier;

import java.nio.file.Path;
import java.util.*;

class GencodeGeneIterator extends BaseGeneIterator<GencodeGene, GencodeTranscriptMetadata, GencodeTranscript> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GencodeGeneIterator.class);

    // These are required attributes for gene and transcript record. Any record lacking one or more attribute is skipped
    // and the warning is logged.
    // Note that `gene_id` is mandatory argument and the GTF lines lacking the `gene_id` do
    // not make it into `GtfRecord`.
    private static final Set<String> MANDATORY_GENE_ATTRIBUTE_NAMES = Set.of("gene_type", "gene_name", "level");
    private static final Set<String> MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES = Set.of("transcript_type", "transcript_name", "level");
    private static final Set<String> MANDATORY_EXON_ATTRIBUTE_NAMES = Set.of("exon_id", "exon_number");
    private static final String NCBI_GENE_ID_IS_NA = null; // Gencode does not provide NCBIGene IDs

    GencodeGeneIterator(Path gencodeGtfPath, GenomicAssembly genomicAssembly) {
        super(gencodeGtfPath, genomicAssembly, MANDATORY_TRANSCRIPT_ATTRIBUTE_NAMES, MANDATORY_EXON_ATTRIBUTE_NAMES);
    }

    @Override
    protected Optional<GeneIdentifier> parseGeneIdentifier(String geneId, GtfRecord gene) {
        String hgncId = gene.firstAttribute("hgnc_id");
        return Optional.of(GeneIdentifier.of(geneId, gene.firstAttribute("gene_name"), hgncId, NCBI_GENE_ID_IS_NA));
    }

    @Override
    protected Optional<GencodeTranscriptMetadata> parseGeneMetadata(String geneId, GtfRecord gene) {
        Optional<Biotype> biotype = parseBiotype(gene.firstAttribute("gene_type"));
        if (biotype.isEmpty()) {
            LOGGER.warn("Unable to parse biotype level `{}` for gene `{}`", gene.attribute("gene_name"), geneId);
            return Optional.empty();
        }

        Optional<EvidenceLevel> evidenceLevel = parseEvidenceLevel(gene.firstAttribute("level"));
        if (evidenceLevel.isEmpty()) {
            LOGGER.warn("Unable to parse evidence level `{}` for gene `{}`", gene.attribute("level"), geneId);
            return Optional.empty();
        }
        return Optional.of(GencodeTranscriptMetadata.of(null, biotype.get(), evidenceLevel.get(), gene.tags()));
    }

    private static Optional<EvidenceLevel> parseEvidenceLevel(String level) {
        switch (level) {
            case "1":
                return Optional.of(EvidenceLevel.VERIFIED);
            case "2":
                return Optional.of(EvidenceLevel.MANUALLY_ANNOTATED);
            case "3":
                return Optional.of(EvidenceLevel.AUTOMATICALLY_ANNOTATED);
            default:
                return Optional.empty();
        }
    }

    @Override
    protected Optional<GencodeTranscript> processTranscript(String txId,
                                                            GtfRecord tx,
                                                            List<GtfRecord> exonRecords,
                                                            GtfRecord startCodon,
                                                            GtfRecord stopCodon) {
        TranscriptIdentifier txIdentifier = TranscriptIdentifier.of(txId, tx.firstAttribute("transcript_name"), tx.firstAttribute("ccdsid"));

        Optional<GencodeTranscriptMetadata> metadata = parseTranscriptMetadata(txId, tx);
        if (metadata.isEmpty())
            return Optional.empty();

        List<Coordinates> exons = processExons(exonRecords);

        Optional<Coordinates> cdsCoordinates = createCdsCoordinates(startCodon, stopCodon, txId, tx);

        return Optional.of(
                GencodeTranscript.of(txIdentifier,
                        tx.location(),
                        exons,
                        metadata.get(),
                        cdsCoordinates.orElse(null))
        );
    }

    @Override
    protected GencodeGene newGeneInstance(GeneIdentifier geneIdentifier,
                                          GenomicRegion location,
                                          List<GencodeTranscript> transcripts,
                                          GencodeTranscriptMetadata geneMetadata) {
        return GencodeGeneImpl.of(geneIdentifier,
                location,
                transcripts,
                geneMetadata);
    }

    private static Optional<GencodeTranscriptMetadata> parseTranscriptMetadata(String txId, GtfRecord tx) {
        Optional<EvidenceLevel> level = parseEvidenceLevel(tx.firstAttribute("level"));
        if (level.isEmpty()) {
            LOGGER.warn("Unable to parse evidence level `{}` for gene `{}`", tx.attribute("level"), txId);
            return Optional.empty();
        }

        TranscriptEvidence evidence = mapEvidenceLevel(level.get());

        Optional<Biotype> biotype = parseBiotype(tx.firstAttribute("transcript_type"));
        if (biotype.isEmpty()) {
            LOGGER.warn("Unable to parse biotype level `{}` for gene `{}`", tx.attribute("gene_name"), txId);
            return Optional.empty();
        }

        return Optional.of(GencodeTranscriptMetadata.of(evidence, biotype.get(), level.get(), tx.tags()));
    }

    private static TranscriptEvidence mapEvidenceLevel(EvidenceLevel evidenceLevel) {
        switch (evidenceLevel) {
            case VERIFIED:
                return TranscriptEvidence.VALIDATED;
            case MANUALLY_ANNOTATED:
                return TranscriptEvidence.MANUAL_ANNOTATION;
            case AUTOMATICALLY_ANNOTATED:
                return TranscriptEvidence.AUTOMATED_ANNOTATION;
            default:
                LOGGER.warn("Unknown evidence level `{}`", evidenceLevel);
                return null;
        }
    }


}
