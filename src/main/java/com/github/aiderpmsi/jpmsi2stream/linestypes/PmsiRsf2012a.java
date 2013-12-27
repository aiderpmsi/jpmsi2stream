package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.regex.Pattern;

/**
 * Définition d'une ligne A de RSF version 2012
 * @author delabre
 *
 */
public class PmsiRsf2012a extends PmsiLineTypeImpl {

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
		{"(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
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
		{null, null}
	};
	
	private static final String name = "RsfA";

	public PmsiRsf2012a() {
		super(name, pattern, names, transforms);
	}
	
}
