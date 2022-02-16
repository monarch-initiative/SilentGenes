package org.monarchinitiative.sgenes.gtf.io.impl;

import org.monarchinitiative.sgenes.gtf.io.gtf.GtfRecord;

import java.util.List;
import java.util.Map;

class GtfGeneData {

    private final GtfRecord gene;
    private final List<GtfRecord> transcripts;
    private final Map<String, List<GtfRecord>> exons;
    private final Map<String, GtfRecord> startCodons;
    private final Map<String, GtfRecord> stopCodons;

    GtfGeneData(GtfRecord gene,
                List<GtfRecord> transcripts,
                Map<String, List<GtfRecord>> exons,
                Map<String, GtfRecord> startCodons,
                Map<String, GtfRecord> stopCodons) {
        this.gene = gene;
        this.transcripts = transcripts;
        this.exons = exons;
        this.startCodons = startCodons;
        this.stopCodons = stopCodons;
    }


    GtfRecord gene() {
        return gene;
    }

    List<GtfRecord> transcripts() {
        return transcripts;
    }

    Map<String, List<GtfRecord>> exonsByTxId() {
        return exons;
    }

    Map<String, GtfRecord> startCodonByTxId() {
        return startCodons;
    }

    Map<String, GtfRecord> stopCodonByTxId() {
        return stopCodons;
    }

}
