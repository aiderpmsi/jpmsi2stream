package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition d'une ligne C de RSF version 2009
 * @author delabre
 *
 */
public class PmsiRsf2009c extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(C)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(.{2})(.{3})(.{1})(.{8})(.{5})" +
			"(.{2})(.{6})(.{2})(.{7})(.{7})(.{3})(.{7})(.{7})(.{6})(.{8})(.{3})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire",
		"NumFacture", "ModeTraitement", "DisciplinePrestation", "JustifExonerationTM", "DateActe",
		"CodeActe", "Quantite", "Coefficient", "Denombrement", "PrixUnitaire", "MontantBaseRemboursementHonoraire",
		"TauxRemboursement", "MontantRemboursableAMOHonoraire", "MontantTotalHonoraire", "MontantRemboursableOCHonoraire",
		"MontantNOEMIE", "OperationNOEMIE"
	};

	private static final String name = "RsfC";

	public PmsiRsf2009c() {
		super(name, pattern, names, null);
	}
}
