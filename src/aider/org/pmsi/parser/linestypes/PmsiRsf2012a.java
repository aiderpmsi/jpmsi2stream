package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

public class PmsiRsf2012a extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(A)(\\d{9})(.{20})(.{1})(.{1})(.{13})(.{2})(.{3})(.{9})(.{1})(.{2})(.{2})(.{1})" +
			"(.{1})(.{2})(.{8})(.{1})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{1})(.{1})(.{9})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "Sexe", "CodeCivilite", "CodeSS", "CleCodeSS",
		"RangBeneficiaire", "NumFacture", "NatureOperation", "NatureAssurance", "TypeContratOC",
		"JustifExonerationTM", "CodePEC", "CodeGdRegime", "DateNaissance", "RangNaissance",
		"DateEntree", "DateSortie", "TotalBaseRemboursementPH", "TotalRemboursableAMOPH",
		"TotalFactureHonoraire", "TotalRemboursableAMOHonoraire", "TotalParticipationAvantOC",
		"TotalRemboursableOCPH", "TotalRemboursableOCHonoraire", "TotalFacturePH", "EtatLiquidation",
		"CMU", "LienMere"
	};

	private static final String name = "RsfA";

	private String[] content = new String[names.length];

	public Pattern getPattern() {
		return pattern;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public String getName() {
		return name;
	}

	public void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	public String[] getContent() {
		return content;
	}
}
