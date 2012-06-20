package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

public class PmsiRsf2009b extends PmsiLineType {
	
	private static final Pattern pattern = Pattern.compile("^(B)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(.{2})(.{3})(.{8})(.{8})" +
			"(.{5})(.{3})(.{1})(.{5})(.{1})(.{5})(.{7})(.{8})(.{3})(.{8})(.{8})(.{7})(.{4})(.{8})(.{3})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire",
		"NumFacture", "ModeTraitement", "DisciplinePrestation", "DateDebutSejour", "DateFinSejour",
		"CodeActe", "Quantite", "JustifExonerationTM", "Coefficient", "CodePEC", "CoefficientMCO",
		"PrixUnitaire", "MontantBaseRemboursementPH", "TauxPrestation", "MontantRemboursableAMOPH",
		"MontantTotalDepense", "MontantRemboursableOCPH", "NumGHS", "MontantNOEMIE", "OperationNOEMIE"
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
