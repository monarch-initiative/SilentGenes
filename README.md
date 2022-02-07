# Silent genes

![Java CI with Maven](https://github.com/ielis/SilentGenes/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.monarchinitiative.sgenes/SilentGenes/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.monarchinitiative.sgenes/SilentGenes)

"Silent companion" library for performing genome arithmetics with genes and transcripts using _[Svart](https://github.com/exomiser/svart)_. 

_Silent genes_ defines models of genes and transcripts. Transcript definitions can be sourced from [Gencode consortium](https://www.gencodegenes.org/), or [Jannovar](https://github.com/charite/jannovar). The definitions can be stored in JSON format. _Silent genes_ works with Java 11 or better.

The project is currently in *pre-alpha* stage.

## Library structure

*Silent genes* consists of several modules:

- `silent-genes-model` provides a model of genes, transcripts, and their associated IDs,
- `silent-genes-io` for serializing `Gene`s and `Transcript`s into JSON to avoid the costly Q/C, and to allow faster parsing,
- `silent-genes-gencode` parses GTF file issued by [Gencode consortium](https://www.gencodegenes.org/) into a list of `Gene`s,
- `silent-genes-jannovar` converts genes and transcripts from the [Jannovar](https://github.com/charite/jannovar) transcript databases into _Silent genes_ format, 
- `silent-genes-simple` gives a handful of real-life genes, mostly useful for unit testing.

## Examples

### Parse Gencode GTF file into a list of genes

First, get the latest GTF file from [Gencode downloads](https://www.gencodegenes.org/human/): 
```bash
wget https://ftp.ebi.ac.uk/pub/databases/gencode/Gencode_human/release_38/gencode.v38.basic.annotation.gtf.gz
```

Next, run something like this to read the GTF file into a list of `GencodeGene`s: 
```java
@Test
public void parseGtfFile() {
    Path gencodeGtf = Path.of("gencode.v38.basic.annotation.gtf.gz");
    GencodeParser gencodeParser = new GencodeParser(gencodeGtf, GenomicAssemblies.GRCh38p13());
    List<GencodeGene> genes = gencodeParser.stream().collect(Collectors.toUnmodifiableList());
    
    assertThat(genes, hasSize(63_116));
}
```


### Serialize genes into compressed JSON

The `silent-genes-io` module provides `GeneParser`s for (de)serializing `Gene`s into flat files. Currently, only JSON 
format is supported.

```java
@Test
public void serializeGenesIntoCompressedJson() {
    List<Gene> genes = ... ; // start with a list of genes, e.g. from the example above
    GeneParserFactory factory = GeneParserFactory.of(GenomicAssemblies.GRCh38p13());
    GeneParser parser = factory.forFormat(SerializationFormat.JSON);

    try (OutputStream os = new BufferedOutputStream(new GzipCompressorOutputStream(new FileOutputStream("genes.json.gz")))) {
        parser.write(genes, os);
    }
}
```

The `genes.json.gz` file with ~63k genes prepared above takes ~9MB of disk space.

### Load the genes from compressed JSON

Load the list of genes from the JSON file created above.

```java
@Test
public void deserializeGenes() {
    Path jsonPath = Path.of("genes.json.gz");
    GeneParserFactory factory = GeneParserFactory.of(GenomicAssemblies.GRCh38p13());
    GeneParser parser = factory.forFormat(SerializationFormat.JSON);

    List<? extends Gene> genes;
    try (InputStream is = new BufferedInputStream(new GZIPInputStream(Files.newInputStream(jsonPath)))) {
        genes = parser.read(is);
    }
}
```
