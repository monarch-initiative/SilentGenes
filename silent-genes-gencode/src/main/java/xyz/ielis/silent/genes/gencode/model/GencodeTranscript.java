package xyz.ielis.silent.genes.gencode.model;


import xyz.ielis.silent.genes.model.Transcript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

public interface GencodeTranscript extends Transcript {

    TranscriptIdentifier id();

    String type();

    EvidenceLevel evidenceLevel();

    // TODO - attributes from https://www.gencodegenes.org/pages/tags.html
    //  e.g. appris, basic...
}
