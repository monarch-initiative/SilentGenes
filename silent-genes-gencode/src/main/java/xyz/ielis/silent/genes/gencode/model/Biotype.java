package xyz.ielis.silent.genes.gencode.model;

/**
 * Gene/Transcript biotypes as described in <a href="https://www.gencodegenes.org/pages/biotypes.html">GENCODE documentation</a>
 * and <a href="https://vega.archive.ensembl.org/info/about/gene_and_transcript_types.html">Vega gene and transcript types</a>.
 */
public enum Biotype {

    /**
     * Sink for miscellaneous types such as artifact.
     */
    unknown,
    /**
     * Contains an open reading frame (ORF).
     */
    protein_coding,
    /**
     * Transcript believed to be protein coding, but with more than one possible open reading frame.
     */
    ambiguous_orf,

    MT,

    /**
     * Does not contain an ORF.
     */
    processed_transcript,


    // ------------------------------------------------- ncRNA ---------------------------------------------------------
    /**
     * Non-coding RNA.
     */
    ncRNA,
    /**
     * MicroRNA.
     */
    miRNA(ncRNA),
    /**
     * Ribosomal RNA.
     */
    rRNA(ncRNA),
    /**
     * Small RNA.
     */
    sRNA(ncRNA),
    /**
     * Transfer RNA.
     */
    tRNA(ncRNA),
    /**
     * piwi-interacting RNA.
     */
    piRNA(ncRNA),
    /**
     * Small conditional RNA.
     */
    scRNA(ncRNA, sRNA),
    /**
     * Small nuclear RNA.
     */
    snRNA(ncRNA, sRNA),
    /**
     * Small nucleolar RNA.
     */
    snoRNA(ncRNA, sRNA),
    /**
     * Small Cajal body-specific RNA.
     */
    scaRNA(ncRNA, sRNA, snoRNA),
    /**
     * Short non coding RNA gene that forms part of the vault ribonucleoprotein complex.
     */
    vaultRNA(ncRNA),
    /**
     * Miscellaneous RNA.
     */
    misc_RNA,

    // ----------------------------------------- Mitochondrial RNA -----------------------------------------------------
    MT_rRNA(MT, rRNA),
    MT_tRNA(MT, tRNA),

    // ----------------------------------------- Nonsense mediated decay -----------------------------------------------
    /**
     * If the coding sequence (following the appropriate reference) of a transcript finishes
     * >50bp from a downstream splice site then it is tagged as NMD.
     * If the variant does not cover the full reference coding sequence then it is annotated as NMD
     * if NMD is unavoidable i.e. no matter what the exon structure of the missing portion is
     * the transcript will be subject to NMD.
     */
    nonsense_mediated_decay,
    /**
     * Transcript that has polyA features (including signal) without a prior stop codon in the CDS,
     * i.e. a non-genomic polyA tail attached directly to the CDS without 3' UTR.
     * These transcripts are subject to degradation.
     */
    non_stop_decay(nonsense_mediated_decay),

    // ------------------------------------------------ lncRNA ---------------------------------------------------------
    /**
     * Generic long non-coding RNA biotype.
     */
    lncRNA(processed_transcript),
    /**
     * Transcript which is known from the literature to not be protein coding.
     */
    non_coding(lncRNA),
    /**
     * Transcript where ditag and/or published experimental data strongly supports the existence of short
     * non-coding transcripts transcribed from the 3'UTR.
     */
    three_prime_overlapping_ncRNA(lncRNA),
    /**
     * Has transcripts that overlap the genomic span (i.e. exon or introns) of a protein-coding locus
     * on the opposite strand.
     */
    antisense(lncRNA),
    /**
     * Long interspersed/intervening noncoding (linc) RNA that can be found in evolutionarily conserved, intergenic regions.
     */
    lincRNA(lncRNA),
    /**
     * Alternatively spliced transcript believed to contain intronic sequence relative to other, coding, variants.
     */
    retained_intron,
    /**
     * Long non-coding transcript in introns of a coding gene that does not overlap any exons.
     */
    sense_intronic(lncRNA),
    /**
     * Long non-coding transcript that contains a coding gene in its intron on the same strand.
     */
    sense_overlapping(lncRNA),
    /**
     * Unspliced lncRNA that is several kb in size.
     */
    macro_lncRNA(lncRNA),
    /**
     * A non-coding locus that originates from within the promoter region of a protein-coding gene,
     * with transcription proceeding in the opposite direction on the other strand.
     */
    bidirectional_promoter_lncRNA(lncRNA),


    // ------------------------------------------------ PSEUDOGENEs ----------------------------------------------------,
    /**
     * Have homology to proteins but generally suffer from a disrupted coding sequence and an active homologous gene
     * can be found at another locus. Sometimes these entries have an intact coding sequence or an open
     * but truncated ORF, in which case there is other evidence used (for example genomic polyA stretches at the 3' end)
     * to classify them as a pseudogene. Can be further classified as one of the following.
     */
    pseudogene,
    /**
     * A species-specific unprocessed pseudogene without a parent gene, as it has an active orthologue in another species.
     */
    unitary_pseudogene(pseudogene),
    /**
     * Pseudogene owing to a SNP/DIP but in other individuals/haplotypes/strains the gene is translated.
     */
    polymorphic_pseudogene(pseudogene),
    /**
     * Pseudogene that lack introns and is thought to arise from reverse transcription of mRNA
     * followed by reinsertion of DNA into the genome.
     */
    processed_pseudogene(pseudogene),
    /**
     * Pseudogene that can contain introns since produced by gene duplication.
     */
    unprocessed_pseudogene(pseudogene),
    /**
     * Pseudogene where protein homology or genomic structure indicates a pseudogene, but the presence
     * of locus-specific transcripts indicates expression.
     */
    transcribed_processed_pseudogene(pseudogene, processed_pseudogene),
    /**
     * Pseudogene where protein homology or genomic structure indicates a pseudogene, but the presence
     * of locus-specific transcripts indicates expression.
     */
    transcribed_unprocessed_pseudogene(pseudogene, unprocessed_pseudogene),
    /**
     * Pseudogene where protein homology or genomic structure indicates a pseudogene, but the presence
     * of locus-specific transcripts indicates expression.
     */
    transcribed_unitary_pseudogene(pseudogene, unitary_pseudogene),
    /**
     * Pseudogene that has mass spec data suggesting that it is also translated.
     */
    translated_processed_pseudogene(pseudogene, processed_pseudogene),
    /**
     * Pseudogene that has mass spec data suggesting that it is also translated.
     */
    translated_unprocessed_pseudogene(pseudogene, unprocessed_pseudogene),
    /**
     * Pseudogene owing to a reverse transcribed and re-inserted sequence.
     */
    retrotransposed_pseudogene(pseudogene),

    /**
     * Immunoglobulin gene.
     */
    IG_gene,
    IG_C_gene(IG_gene),
    IG_D_gene(IG_gene),
    IG_J_gene(IG_gene),
    IG_LV_gene(IG_gene),
    IG_V_gene(IG_gene),

    /**
     * T cell receptor gene.
     */
    TR_gene,
    TR_C_gene(TR_gene),
    TR_J_gene(TR_gene),
    TR_V_gene(TR_gene),
    TR_D_gene(TR_gene),

    /**
     * Inactivated immunoglobulin gene.
     */
    IG_pseudogene(IG_gene),
    IG_C_pseudogene(IG_pseudogene),
    IG_J_pseudogene(IG_pseudogene),
    IG_V_pseudogene(IG_pseudogene),

    TR_pseudogene,
    TR_C_pseudogene(TR_pseudogene),
    TR_J_pseudogene(TR_pseudogene),
    TR_V_pseudogene(TR_pseudogene),
    TR_D_pseudogene(TR_pseudogene);

    private final Biotype baseType;
    private final Biotype subType;
    private final Biotype subSubType;

    Biotype() {
        this.baseType = this;
        this.subType = this;
        this.subSubType = this;
    }

    Biotype(Biotype parent) {
        this.baseType = parent;
        this.subType = this;
        this.subSubType = this;
    }

    Biotype(Biotype parent, Biotype subType) {
        this.baseType = parent;
        this.subType = subType;
        this.subSubType = this;
    }

    Biotype(Biotype parent, Biotype subType, Biotype subSubType) {
        this.baseType = parent;
        this.subType = subType;
        this.subSubType = subSubType;
    }

    public Biotype getSubSubType() {
        return subSubType;
    }

    public Biotype getBaseType() {
        return baseType;
    }

    public Biotype getSubType() {
        return subType;
    }


}
