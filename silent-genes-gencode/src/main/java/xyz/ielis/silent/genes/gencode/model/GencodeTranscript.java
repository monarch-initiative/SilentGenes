package xyz.ielis.silent.genes.gencode.model;


import xyz.ielis.silent.genes.model.Transcript;

public interface GencodeTranscript extends Transcript {

    String id();

    String type();

    Status status();

    String name();

    EvidenceLevel evidenceLevel();

}
