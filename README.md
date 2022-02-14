# Silent genes

[![Java CI with Maven](https://github.com/ielis/SilentGenes/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/ielis/SilentGenes/actions/workflows/maven.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.monarchinitiative.sgenes/SilentGenes/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.monarchinitiative.sgenes/SilentGenes)

"Silent companion" library for performing genome arithmetics with genes and transcripts using _[Svart](https://github.com/exomiser/svart)_. 

_Silent genes_ defines models of genes and transcripts. Transcript definitions can be sourced from [Gencode consortium](https://www.gencodegenes.org/), or [Jannovar](https://github.com/charite/jannovar). The definitions can be stored in JSON format. _Silent genes_ works with Java 11 or better.

The project is currently in *pre-alpha* stage.

## Library structure

*Silent genes* consists of several modules:

- `silent-genes-model` provides a model of genes, transcripts, and their associated IDs,
- `silent-genes-io` for serializing `Gene`s and `Transcript`s in the internal data format into a JSON file to avoid the costly Q/C,
- `silent-genes-gtf` parses GTF file issued by [Gencode consortium](https://www.gencodegenes.org/) or [NCBI Reference Sequence project](https://www.ncbi.nlm.nih.gov/refseq),
- `silent-genes-jannovar` converts genes and transcripts from the [Jannovar](https://github.com/charite/jannovar) transcript databases into _Silent genes_ format, 
- `silent-genes-simple` gives a handful of real-life genes, mostly useful for unit testing, and
- `silent-genes-cli` command-line interface for converting genes into the internal data format.

## Command-line interface (CLI)

_Silent genes_ offeres CLI for converting gene and transcript definitions into the internal format. The following 
sources of genes and transcripts are supported:

- _Gencode_ (GTF) - genes provided by [Gencode consortium](https://www.gencodegenes.org/)
- _RefSeq_ (GTF) - genes defined by [NCBI Reference Sequence](https://www.ncbi.nlm.nih.gov/refseq) (RefSeq) project
- _Jannovar_ - [Jannovar](https://github.com/charite/jannovar) is a Java tool for functional annotation of genomic variants identified in NGS experiments, and it uses transcripts defined by _RefSeq_, _ENSEMBL_, and _UCSC_.

### Convert Gencode genes into _Silent genes_ format

Use `parse-gencode` to convert Gencode genes:

```bash
wget https://ftp.ebi.ac.uk/pub/databases/gencode/Gencode_human/release_39/gencode.v39.basic.annotation.gtf.gz
java -jar silent-genes-cli.jar parse-gencode gencode.v39.basic.annotation.gtf.gz gencode.sg.json.gz
```

### Convert Refseq genes into _Silent genes_ format

Use `parse-refseq` to perform the conversion:

```bash
wget https://ftp.ncbi.nlm.nih.gov/genomes/refseq/vertebrate_mammalian/Homo_sapiens/annotation_releases/109.20211119/GCF_000001405.39_GRCh38.p13/GCF_000001405.39_GRCh38.p13_genomic.gtf.gz
java -jar silent-genes-cli.jar parse-refseq GCF_000001405.39_GRCh38.p13_genomic.gtf.gz refseq.sg.json.gz
```

## Code examples

The next paragraphs show how to use _Silent genes_ as a library in other Java application.

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
