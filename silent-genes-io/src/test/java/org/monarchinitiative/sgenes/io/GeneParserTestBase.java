package org.monarchinitiative.sgenes.io;

import org.monarchinitiative.svart.assembly.GenomicAssemblies;
import org.monarchinitiative.svart.assembly.GenomicAssembly;

import java.nio.file.Path;

public class GeneParserTestBase {

    protected static final GenomicAssembly ASSEMBLY = GenomicAssemblies.GRCh38p13();

    protected static final Path IO_TEST_BASE = Path.of("src/test/resources/org/monarchinitiative/sgenes/io");

    protected static final Path JSON_TEST = IO_TEST_BASE.resolve("json").resolve("test-surf1_surf2.json");

}
