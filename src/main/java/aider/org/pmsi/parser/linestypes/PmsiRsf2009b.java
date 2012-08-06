package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition d'une ligne B de RSF version 2009
 * @author delabre
 *
 */
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

	private static final String name = "RsfB";
	
	private String[] content = new String[names.length];

	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public String[] getNames() {
		return names;
	}
		
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	@Override
	public String[] getContent() {
		return content;
	}
}
