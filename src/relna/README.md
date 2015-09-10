### INSTALLATION ###

1. Install `gradle` on your system.
2. Unzip the `genia.mod.tar.gz` from `resources/` and copy it to `src/main/resources/gimli/resources/tools/gdep`
3. Navigate to the parent directory (where build.gradle is placed) and run `gradle clean build`

### RUNNING THE TOOL ###

Navigate to `build/libs` and run the tool with `java -jar BiomedicalTextMiningPipeline.jar` with the following arguments:
* -i **path-to-input-file** 
* -o **path-to-output-file**
* -w **writer-format - anndoc or json**
* -r **reader-format - can be txt or iob2 (optional argument)**

If `reader format (-r)` argument is not given, the program will guess based on the extension.

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
