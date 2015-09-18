### JNLPBA corpus

* The corpus is originally distributed in the iob2 format, having a training and test dataset.
* The training dataset is 2000 MEDLINE abstracts filtered for the MeSH terms 'human', 'blood' and 'transcription factors'.
* The details of how the dataset was annotated is not explicitly mentioned, and the only source for the paper is provided [here](http://www.nactem.ac.uk/tsujii/GENIA/ERtask/shared_task_intro.pdf).
* We converted the JNLPBA corpus to the TagTog format, whose guidelines are published [here](https://github.com/jmcejuela/tagtog-doc/wiki)
* Since the corpus is manually annotated by the Genia folks, in the ann.json file, we write `who: ["user:genia4er"]`.
* The JNLPBA corpus consists of only abstracts. We artificially categorize the abstract into title and abstract. The first line constitutes the title, and the subsequent lines constitute the abstract. Each journal article abstract is separated by "###MEDLINE:<medline_id>".
