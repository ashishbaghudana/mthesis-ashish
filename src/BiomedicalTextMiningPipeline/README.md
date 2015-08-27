### INSTALLATION ###

1. Install `gradle` on your system.
2. Unzip the `genia.mod.tar.gz` from `resources/` and copy it to `src/main/resources/gimli/resources/tools/gdep`
3. Navigate to the parent directory (where build.gradle is placed) and run `gradle clean build`

### RUNNING THE TOOL ###

Navigate to `build/libs` and run the tool with `java -jar BiomedicalTextMiningPipeline.jar` with the following arguments:
* -c **path-to-corpus-in-IOB-format** 
* -g **path-to-load-or-save-gdep**
* -o **path-to-output-with-suffix-iob2**
* -f **input format - txt or iob2**

**Example**

`java -jar build/libs/BiomedicalTextMiningPipeline-1.0.jar -c sample/txt/corpus2.txt -f txt -g sample/txt/gdep.gz -o sample/txt/output2.iob2`

`java -jar build/libs/BiomedicalTextMiningPipeline-1.0.jar -c sample/txt/corpus1.iob2 -f txt -g sample/iob2/gdep.gz -o sample/iob2/output1.iob2`

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
