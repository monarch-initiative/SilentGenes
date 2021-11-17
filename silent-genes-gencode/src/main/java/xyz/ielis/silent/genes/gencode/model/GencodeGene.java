package xyz.ielis.silent.genes.gencode.model;

import xyz.ielis.silent.genes.model.Gene;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface GencodeGene extends Gene {

    Biotype biotype();

    EvidenceLevel evidenceLevel();

    @Override
    Iterator<? extends GencodeTranscript> transcripts();

    Set<String> tags();

    @Override
    default Stream<? extends GencodeTranscript> transcriptStream() {
        return StreamSupport.stream(Spliterators.spliterator(transcripts(), transcriptCount(),
                Spliterator.DISTINCT & Spliterator.SIZED), false);
    }
}
