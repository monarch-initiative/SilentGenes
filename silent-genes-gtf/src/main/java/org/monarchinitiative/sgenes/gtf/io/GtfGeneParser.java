package org.monarchinitiative.sgenes.gtf.io;

import org.monarchinitiative.sgenes.model.Gene;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface GtfGeneParser<T extends Gene> extends Iterable<T> {

    default Stream<T> stream() {
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator(), Spliterator.IMMUTABLE);
        return StreamSupport.stream(spliterator, false);
    }

}
