package xyz.ielis.silent.genes.gencode.model;

import xyz.ielis.silent.genes.model.Gene;

import java.util.Set;
import java.util.stream.Stream;

public interface GencodeGene extends Gene {

    Biotype biotype();

    EvidenceLevel evidenceLevel();

    @Override
    Stream<? extends GencodeTranscript> transcripts();

    Set<String> tags();
}
