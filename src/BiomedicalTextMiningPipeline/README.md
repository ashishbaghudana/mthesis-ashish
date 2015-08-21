### INSTALLATION ###

1. Install `gradle` on your system.
2. Unzip the `genia.mod.tar.gz` from `resources/` and copy it to `src/main/resources/gimli/resources/tools/gdep`
3. Navigate to the parent directory (where build.gradle is placed) and run `gradle clean build`

### RUNNING THE TOOL ###

Navigate to `build/libs` and run the tool with `java -jar BiomedicalTextMiningPipeline.jar` with the following arguments:
* -c _path-to-corpus-in-IOB-format_
* -g _path-to-load-or-save-gdep_
* -o _path-to-output-with-suffix-iob2_
