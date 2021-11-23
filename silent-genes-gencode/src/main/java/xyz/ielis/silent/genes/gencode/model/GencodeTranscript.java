package xyz.ielis.silent.genes.gencode.model;


import xyz.ielis.silent.genes.model.Transcript;
import xyz.ielis.silent.genes.model.TranscriptIdentifier;

import java.util.Set;

public interface GencodeTranscript extends Transcript {

    TranscriptIdentifier id();

    Biotype biotype();

    EvidenceLevel evidenceLevel();

    Set<String> tags();
}
