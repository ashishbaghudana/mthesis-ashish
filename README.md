# Status

* NLPBA corpus of 2000 abstracts with tag annotations of `Protein`, `DNA`, and `RNA`. No relations
* LLL05 corpuswith 85 sentences with relations of `genic` interaction (to anything)

# Goal

1. Expand NLPBA corpus with:
  * `Protein` to `RNA` relations (reason, more interesting biologically & we don't have to deal with the problem of distinguishing Protein vs Genes)
  * `Protein` to `DNA elements` (very specific cases... TODO)
  * [ ] Write as PubAnnotation format & anndoc (tagtog) format
2. Create method to given a text or PMID, recognize these kind of relations (meaning, entity names have to also be recognized)
  * Make method public
  * Well documented for totally new users (HOWTO)

# Approach

1. Expand dictionary by bootstrapping method:
  1. Annotate seed corpus of 20 abstracts (with relations)
  2. Develop method for relationship extraction
  3. Predict on a bigger set of abstracts
  4. Review manually (gradually check only low confidence cases)
  5. Add to whole corpus and repeat process
