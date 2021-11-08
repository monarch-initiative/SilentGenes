# Silent genes

![Java CI with Maven](https://github.com/ielis/SilentGenes/workflows/Java%20CI%20with%20Maven/badge.svg)

A library that lets you silently work with genes and transcripts.

The project is currently in *pre-alpha* stage.

## Library structure

*Silent genes* consists of several modules:

- `silent-genes-model` defines the common properties of genes and transcripts,
- `silent-genes-gencode` provides genes and transcripts as specified by [Gencode consortium](https://www.gencodegenes.org/),
- `silent-genes-jannovar` provides genes and transcripts from the [Jannovar](https://github.com/charite/jannovar) transcript databases,
- `silent-genes-simple` gives you a handful of real-life genes, mostly useful for unit testing

## Example

Parse Gencode GTF file into a list of genes.

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
