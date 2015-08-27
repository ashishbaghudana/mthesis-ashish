package org.rostlab.relna.config;

public class Constants {
	public static final String PROTEIN = "protein";
	public static final String DNA = "DNA";
	public static final String RNA = "RNA";
	
	public static final String B = "B-";
	public static final String I = "I-";
	public static final String O = "O";
	
	public static final String B_PROTEIN = B.concat(PROTEIN);
	public static final String I_PROTEIN = I.concat(PROTEIN);
	
	public static final String B_DNA = B.concat(DNA);
	public static final String I_DNA = I.concat(DNA);
	
	public static final String B_RNA = B.concat(RNA);
	public static final String I_RNA = I.concat(RNA);
	
	public static final String GIMLI_RESOURCES = "src/main/resources/gimli/";
	
	private static final String [] CRFS = {"models/crf_protein.gz", "models/crf_dna.gz", "models/crf_rna.gz"};
	private static final String [] CONFIG = {"config/jnlpba_dna.config", "config/jnlpba_protein.config", "config/jnlpba_rna.config"};
	
	public static final String PROTEIN_MODEL = GIMLI_RESOURCES.concat(CRFS[0]);
	public static final String DNA_MODEL = GIMLI_RESOURCES.concat(CRFS[1]);
	public static final String RNA_MODEL = GIMLI_RESOURCES.concat(CRFS[2]);
	
	public static final String PROTEIN_CONFIG = GIMLI_RESOURCES.concat(CONFIG[0]);
	public static final String DNA_CONFIG = GIMLI_RESOURCES.concat(CONFIG[1]);
	public static final String RNA_CONFIG = GIMLI_RESOURCES.concat(CONFIG[2]);
	
	public static final String [] FEATURES = {PROTEIN_CONFIG, DNA_CONFIG, RNA_CONFIG};
	public static final String [] MODELS = {PROTEIN_MODEL, DNA_MODEL, RNA_MODEL};
	
	public static final int TITLE = 0;
	public static final int ABSTRACT = 1;
}
