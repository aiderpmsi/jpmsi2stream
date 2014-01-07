package com.github.aiderpmsi.jpmsi2stream.linestypes;

/**
 * Définition d'une ligne B de RSF version 2009
 * @author delabre
 *
 */
public class PmsiRsf2009b extends PmsiLineTypeImpl {
	
	private static final String[][] definitions = {
		{"TypeEnregistrement", "(B)", null, null},
		{"Finess", "(\\d{9})", null, null},
		{"NumRSS", "(.{20})", null, null},
		{"CodeSS", "(.{13})", null, null},
		{"CleCodeSS", "(.{2})", null, null},
		{"RangBeneficiaire", "(.{3})", null, null},
		{"NumFacture", "(.{9})", null, null},
		{"ModeTraitement", "(.{2})", null, null},
		{"DisciplinePrestation", "(.{3})", null, null},
		{"DateDebutSejour", "(.{8})", "(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
		{"DateFinSejour", "(.{8})", "(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
		{"CodeActe", "(.{5})", null, null},
		{"Quantite", "(.{3})", null, null},
		{"JustifExonerationTM", "(.{1})", null, null},
		{"Coefficient", "(.{5})", null, null},
		{"CodePEC", "(.{1})", null, null},
		{"CoefficientMCO", "(.{5})", null, null},
		{"PrixUnitaire", "(.{7})", null, null},
		{"MontantBaseRemboursementPH", "(.{8})", null, null},
		{"TauxPrestation", "(.{3})", null, null},
		{"MontantRemboursableAMOPH", "(.{8})", null, null},
		{"MontantTotalDepense", "(.{8})", null, null},
		{"MontantRemboursableOCPH", "(.{7})", null, null},
		{"NumGHS", "(.{4})", null, null},
		{"MontantNOEMIE", "(.{8})", null, null},
		{"OperationNOEMIE", "(.{3})", null, null}
	};
	
	private static final String name = "RsfB";
	
	public PmsiRsf2009b() {
		super(definitions, name);
	}
}
