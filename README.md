## Status

* NLPBA corpus of 2000 abstracts with tag annotations of `Protein`, `DNA`, and `RNA`. No relations
* LLL05 corpuswith 85 sentences with relations of `genic` interaction (to anything)
* Stub implementation of [Relna](https://github.com/ashishbaghudana/mthesis-ashish/) here.
* [README](https://github.com/ashishbaghudana/mthesis-ashish/) for Relna follows below.

## Goal

1. Expand NLPBA corpus with:
  * `Protein` to `RNA` relations (reason, more interesting biologically & we don't have to deal with the problem of distinguishing Protein vs Genes)
  * `Protein` to `DNA elements` (very specific cases... TODO)
  * [ ] Write as PubAnnotation format & anndoc (tagtog) format
2. Create method to given a text or PMID, recognize these kind of relations (meaning, entity names have to also be recognized)
  * Make method public
  * Well documented for totally new users (HOWTO)

## Approach

1. Expand dictionary by bootstrapping method:
  1. Annotate seed corpus of 20 abstracts (with relations)
  2. Develop method for relationship extraction
  3. Predict on a bigger set of abstracts
  4. Review manually (gradually check only low confidence cases)
  5. Add to whole corpus and repeat process

# RELNA HOWTO

### INSTALLATION ###

1. Install `gradle` on your system.
2. Unzip the `genia.mod.tar.gz` from `resources/` and copy it to `src/main/resources/gimli/resources/tools/gdep`
3. Navigate to the parent directory (where build.gradle is placed) and run `gradle clean build`

### RUNNING THE TOOL ###

Navigate to `build/libs` and run the tool with `java -jar BiomedicalTextMiningPipeline.jar` with the following arguments:
* `-i` **path-to-input-file** 
* `-o` **path-to-output-file**
* `-w` **writer-format - anndoc or json**
* `-r` **reader-format - can be txt or iob2 (optional argument)**
* `-s` **string - text to tag given from command line (optional argument)**

If `reader format (-r)` argument is not given, the program will guess based on the extension.

If `string (-s)` argument is given, the program will automatically read from the command line for an input.

#### Example ####

**Input format : iob2, `-r` specified, writer format : anndoc**
`java -jar build/libs/BiomedicalTextMiningPipeline-1.0.jar -r iob2 -i sample/iob2/corpus1.iob2 -w anndoc -o sample/iob2/output1.ann.json`

**Input format : iob2, `-r` not specified, writer format : json**
`java -jar build/libs/BiomedicalTextMiningPipeline-1.0.jar -i sample/iob2/corpus1.iob2 -w json -o sample/iob2/output1.json`

**Input format : txt, `-r` specified, writer format : anndoc**
`java -jar build/libs/BiomedicalTextMiningPipeline-1.0.jar -i sample/txt/corpus2.txt -r txt -w anndoc -o sample/txt/output2.ann.json`

If the file is in `txt` format, it must follow the pattern below.

```text
<identifier>
<title>
<abstract>
```

If the file is in `iob` format, it must follow the pattern below.

```text
<identifier>

<sentence begin>
.
.
<token>
.
.
<sentence end>

<sentence begin>
.
.
.
<sentence end>
```
