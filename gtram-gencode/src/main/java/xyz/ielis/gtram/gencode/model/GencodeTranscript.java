package xyz.ielis.gtram.gencode.model;


import xyz.ielis.gtram.model.Transcript;

public interface GencodeTranscript extends Transcript {

    String id();

    String type();

    Status status();

    String name();

    EvidenceLevel evidenceLevel();

}
