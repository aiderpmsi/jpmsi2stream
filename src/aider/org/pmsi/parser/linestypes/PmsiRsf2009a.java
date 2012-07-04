package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

public class PmsiRsf2009a extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(A)(\\d{9})(.{20})(.{1})(.{1})(.{13})(.{2})(.{3})(.{9})(.{1})(.{2})(.{2})(.{1})" +
			"(.{1})(.{2})(.{8})(.{1})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{8})(.{1})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "Sexe", "CodeCivilite", "CodeSS", "CleCodeSS",
		"RangBeneficiaire", "NumFacture", "NatureOperation", "NatureAssurance", "TypeContratOC",
		"JustifExonerationTM", "CodePEC", "CodeGdRegime", "DateNaissance", "RangNaissance",
		"DateEntree", "DateSortie", "TotalBaseRemboursementPH", "TotalRemboursableAMOPH",
		"TotalFactureHonoraire", "TotalRemboursableAMOHonoraire", "TotalParticipationAvantOC",
		"TotalRemboursableOCPH", "TotalRemboursableOCHonoraire", "TotalFacturePH", "EtatLiquidation"
	};
	
	private static final String[][] transforms = {
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{"(\\d{4})(\\d{2})(\\d{2})", "$3-$2-$1"},
		{null, null},
		{"(\\d{4})(\\d{2})(\\d{2})", "$3-$2-$1"},
		{"(\\d{4})(\\d{2})(\\d{2})", "$3-$2-$1"},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null}
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
		String[] modContent = new String[names.length];
		for (int i = 0 ; i < names.length ; i++) {
			if (transforms[i][0] == null)
				modContent[i] = content[i];
			else {
				modContent[i] = content[i].replaceFirst(transforms[i][0], transforms[i][1]);
			}
		}
		return modContent;
	}
}
