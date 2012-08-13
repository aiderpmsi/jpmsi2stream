package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition d'une ligne B de RSF version 2009
 * @author delabre
 *
 */
public class PmsiRsf2009b extends PmsiLineTypeImpl {
	
	private static final Pattern pattern = Pattern.compile("^(B)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(.{2})(.{3})(.{8})(.{8})" +
			"(.{5})(.{3})(.{1})(.{5})(.{1})(.{5})(.{7})(.{8})(.{3})(.{8})(.{8})(.{7})(.{4})(.{8})(.{3})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire",
		"NumFacture", "ModeTraitement", "DisciplinePrestation", "DateDebutSejour", "DateFinSejour",
		"CodeActe", "Quantite", "JustifExonerationTM", "Coefficient", "CodePEC", "CoefficientMCO",
		"PrixUnitaire", "MontantBaseRemboursementPH", "TauxPrestation", "MontantRemboursableAMOPH",
		"MontantTotalDepense", "MontantRemboursableOCPH", "NumGHS", "MontantNOEMIE", "OperationNOEMIE"
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
		{"(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
		{"(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
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
		{null, null}
	};
	
	private static final String name = "RsfB";
	
	public PmsiRsf2009b() {
		super(name, pattern, names, transforms);
	}
}
