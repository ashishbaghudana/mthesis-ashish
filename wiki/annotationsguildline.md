## Documents for Annotation
* We annotate PubMed articles (abstracts & PubMed Central full-text articles):
    * Number of Abstracts: 100
    * PubMed articles are randomly sampled from the JNLPBA corpus. These have already been annotated with entity labels from the 5 classes - Protein, DNA, RNA, Cell Line and Cell Type.
      * The corpus was formed by filtering MEDLINE entries using the MeSH terms 'human', 'blood cells' and 'transcription factors'.
      * 2,000 abstracts were manually annotated for 36 terminal classes, which were then simplified to the 5 mentioned above, for the BioNLP/NLPBA 2004 task.
      * The publication year for all the training documents is within 1990-99.  
      * More information about the JNLPBA corpus is available [here](http://www.nactem.ac.uk/tsujii/GENIA/ERtask/report.html)

# Annotation Information

The entity classes follow JNLPBA corpus guidelines, with the 5 classes - Protein, DNA, RNA, Cell Type and Cell Line. We make use of only 3 out of the 5 classes:
    * `Proteins`
    * `DNA`
    * `RNA`

We annotate the relations:
    * `Proteins` <--> `DNA`
    * `Proteins` <--> `RNA`

## Annotation Rules

To make rules clear, examples are given for most rules. For the sake of brevity, examples which are originally of the format
```text
a	O
component	O
of	O
the	O
m6A	B-protein
RNA	I-protein
methyltransferase	I-protein
complex	I-protein
```

are written as `a/O component/O of/O the/O m6A/B-protein RNA/I-protein methyltransferase/I-protein complex/I-protein`


### Entity Classes: Protein, DNA, RNA

* **Annotation Format**: The JNLPBA corpus is tagged in the BIO format - beginning, inside and outside. One entity consists of the phrase originating from `B-`to `O` - no more, no less. Even if the tag contains articles such as `a, an, the`, conjunctions such as `of, for, and`, open parenthesis, closed parenthesis or commas, if its tag is not `O`, it automatically is (a part of) an entity.

* **Entity ranges**: The BIO format defines entity ranges specifically with `B` as the starting of the entity, `I` as the continuation of that entity and the first `O` as the end of the entity. e.g. `The/O katX/B-DNA gene/I-DNA of/O Bacillus/O subtilis/O`. The entity here is _katX gene_, which is of the class DNA.

* **Rule of acronyms**: If the acronym is part of the entity (as given by the rules above), it will be included in the entity name. eg. `Anti-Ro/B-protein (/I-protein SSA/I-protein )/I-protein autoantibodies/I-protein`. `(SSA)` will be part of the entity.

* **Entity Length**: An entity has to be of a minimum length 1 and can be arbitrarily long.

* **One Structure, Multiple Entities**: The JNLPBA corpus acknowledges that there may be multiple entities within one structure. However, for our purposes, one structure counts as one entity. eg. `sigmaB/B-protein and/I-protein sigmaF/I-protein` counts as one entity, even though _sigmaB_ and _sigmaF_ are different proteins.

* **Divided Mentions**: In certain cases, the same entity is divided into two fragments by a token. In such cases, the two will count as separate entities. eg. `T/B-protein cell/I-protein receptors/I-protein (/O TCRs/B-protein )/O`. _T cell receptors_ and _TCRs_ will both be separate entities.

* **Different Mentions**: In certain other cases, the first entity might be complete and the others might be qualified by the first. As long as they the entities are encapsulated in separate structures, they qualify as separate entities. eg. `SPOC/B-protein domain/I-protein RNA/I-protein binding/I-protein proteins/I-protein Rbm15/B-protein and/O Spen/B-protein and/O Wtap/B-protein`. The proteins mentioned in this sentence would be _SPOC domain RNA binding proteins_, _Rbm15_, _Spen_, and	_Wtap_.

### Relations

#### Rules for Protein-DNA relations:

* Protein - DNA relationships are mainly of the types
  * **Gene Expression**:
    * ID: _[GO:0010467]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0010467)
    * Def: The process in which a gene's sequence is converted into a mature gene product or products (proteins or RNA). This includes the production of an RNA transcript as well as any processing to produce a mature RNA product or an mRNA (for protein-coding genes) and the translation of that mRNA into protein. Protein maturation is included when required to form an active form of a product from an inactive precursor form.
    * **Transcription**:
      * ID: _[GO:0006351]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0006351)
      * Def: The cellular synthesis of RNA on a template of DNA.
  * **DNA Repair**:
    * ID: _[GO:0006281]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0006281)
    * Def: The process of restoring DNA after damage. Genomes are subject to damage by chemical and physical agents in the environment (e.g. UV and ionizing radiations, chemical mutagens, fungal and bacterial toxins, etc.) and by free radicals or alkylating agents endogenously generated in metabolism. DNA is also damaged because of errors during its replication. A variety of different DNA repair pathways have been reported that include direct reversal, base excision repair, nucleotide excision repair, photoreactivation, bypass, double-strand break repair pathway, and mismatch repair pathway.
  * **Regulation of Gene Silencing**:
    * ID: _[GO:0060968]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0060968)
    * Any process that modulates the rate, frequency, or extent of gene silencing, the transcriptional or post-transcriptional process carried out at the cellular level that results in long-term gene inactivation.
    * Look for gene silencing governed by proteins only. Gene silencing can be otherwise performed by RNA as well, and can also be done at the mRNA level.
  * **Chromatin Remodelling**:
    * ID: _[GO:0006338]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0006338)
    * Def: Dynamic structural changes to eukaryotic chromatin occurring throughout the cell division cycle. These changes range from the local changes necessary for transcriptional regulation to global changes necessary for chromosome segregation.

* Try to relate mention of DNA entity instances to the closest protein entity. It is likely that some mentions of DNA entities are unrelated to any protein entity. In these cases, do not forcefully annotate a relationship where one does not exist.
  * Closest means shortest in number of separating words.
  * **TODO** Define a standard window size to look for relationships.

* Look for Protein-DNA relationships that fit the above criteria only, _do not annotate relationships if they record any different kind of relationships_

* If a certain DNA entity element is _related_ to more than one protein element, all relationships should be recorded.

* If a certain DNA entity element is _related_ to the same protein, given by two different names (especially if the protein name is abbreviated), both relationships should be recorded.

* Possible trigger words that indiciate a protein - nucleic acid relationship are : `"control", "inhibit", "increase", "restrict", "regul", "translat", "transcript", "transcrib", "affect", "effect", "bind", "splice", "suppress", "augment", "declin", "stimul", "activ", "induc", "induct", "depend", "requir", "accumul"`.

#### Rules for Protein-RNA relations:

* Protein-RNA relationships are mostly of the types:
  * **Translation**:
    * ID: _[GO:0006412]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0006412)
    * Def: The cellular metabolic process in which a protein is formed, using the sequence of a mature mRNA molecule to specify the sequence of amino acids in a polypeptide chain. Translation is mediated by the ribosome, and begins with the formation of a ternary complex between aminoacylated initiator methionine tRNA, GTP, and initiation factor 2, which subsequently associates with the small subunit of the ribosome and an mRNA. Translation ends with the release of a polypeptide chain from the ribosome.
  * **RNA Splicing**:
    * ID: _[GO:0008380]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0008380)
    * Def: The process of removing sections of the primary RNA transcript to remove sequences not present in the mature form of the RNA and joining the remaining sections to form the mature form of the RNA.
  * **RNA Modification or RNA Editing**:
    * ID: _[GO:0009451]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0009451)
    * Def: The covalent alteration of one or more nucleotides within an RNA molecule to produce an RNA molecule with a sequence that differs from that coded genetically.
  * **RNA Processing**:
    * ID: _[GO:0006396]_(https://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0006396)
    * Def: Any process involved in the conversion of one or more primary RNA transcripts into one or more mature RNA molecules.

* Follow the same guidelines as protein - DNA relationships for protein - RNA relationships as well.

#### _Meaningful Relation_ (definition)

* A relation is meaningful if either of following two conditions is true:
      1. We can find the written _words_ used by the author that implies that there can be a relation (in this case, the relation is not inferred):
        * Example: "The `katX gene` of Bacillus subtilis is under dual control of `sigmaB` and `sigmaF`".  In the given example, we have the _words_ that clearly suggests that `katX gene` is related to `sigmaB` and `sigmaF` under the `gene expression` GO ontology. We do not need to infer anything here.

      2. We can **infer** the relation from the context:
        * Example: "`IL-2 gene` expression and `NF-kappa B` activation through CD28 requires reactive oxygen production by `5-lipoxygenase`". Even though `5-lipoxygenase` does not directly cause transcription of `IL-2 gene`, the sentence implies that `IL-2 gene` expression _depends_ on `5-lipoxygenase`. This would be annotated under the GO ontology `gene expression`.

#### General rules for relations:

* _Always_ if you relate the long name (or abbreviation) , do relate the abbreviation (or, respectively, long name) as well .
* Names almost equal but still spelt slightly differently should be considered as "different entities" for annotation purposes. That is, when considering relations, you should relate both entities to whatever is needed.
