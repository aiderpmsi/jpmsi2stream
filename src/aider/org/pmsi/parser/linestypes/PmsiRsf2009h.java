package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;


public class PmsiRsf2009h extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(H)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(.{8})(.{7})(.{5})(.{7})" +
			"(.{7})(.{7})(.{3})(.{7})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire", "NumFacture",
		"DateDebutSejour", "CodeUCD", "CoefficientFractionnement", "PrixAchatUnitaire", "MontantUnitaireEcartIndemnisable",
		"MontantTotalEcartIndemnisable", "Quantite", "MontantTotalFactureTTC"
	};

	private String[] content = new String[names.length];

	public Pattern getPattern() {
		return pattern;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	public String[] getContent() {
		return content;
	}
}
