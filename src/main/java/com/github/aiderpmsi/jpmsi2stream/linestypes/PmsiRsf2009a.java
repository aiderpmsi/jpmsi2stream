package com.github.aiderpmsi.jpmsi2stream.linestypes;

/**
 * Définition d'une ligne A de RSF version 2009
 * @author delabre
 *
 */
public class PmsiRsf2009a extends PmsiLineTypeImpl {

	private static final String[][] definitions = {
		{"TypeEnregistrement", "(A)", null, null},
		{"Finess", "(\\d{9})", null, null},
		{"NumRSS", "(.{20})", null, null},
		{"Sexe", "(.{1})", null, null},
		{"CodeCivilite", "(.{1})", null, null},
		{"CodeSS", "(.{13})", null, null},
		{"CleCodeSS", "(.{2})", null, null},
		{"RangBeneficiaire", "(.{3})", null, null},
		{"NumFacture", "(.{9})", null, null},
		{"NatureOperation", "(.{1})", null, null},
		{"NatureAssurance", "(.{2})", null, null},
		{"TypeContratOC", "(.{2})", null, null},
		{"JustifExonerationTM", "(.{1})", null, null},
		{"CodePEC", "(.{1})", null, null},
		{"CodeGdRegime", "(.{2})", null, null},
		{"DateNaissance", "(.{8})", "(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
		{"RangNaissance", "(.{1})", null, null},
		{"DateEntree", "(.{8})", "(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
		{"DateSortie", "(.{8})", "(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
		{"TotalBaseRemboursementPH", "(.{8})", null, null},
		{"TotalRemboursableAMOPH", "(.{8})", null, null},
		{"TotalFactureHonoraire", "(.{8})", null, null},
		{"TotalRemboursableAMOHonoraire", "(.{8})", null, null},
		{"TotalParticipationAvantOC", "(.{8})", null, null},
		{"TotalRemboursableOCPH", "(.{8})", null, null},
		{"TotalRemboursableOCHonoraire", "(.{8})", null, null},
		{"TotalFacturePH", "(.{8})", null, null},
		{"EtatLiquidation", "(.{1})", null, null}
	};
	
	private static final String name = "RsfA";

	public PmsiRsf2009a() {
		super(definitions, name);
	}
}
