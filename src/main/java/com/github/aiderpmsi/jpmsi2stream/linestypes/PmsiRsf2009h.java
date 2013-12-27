package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.regex.Pattern;

/**
 * Définition d'une ligne H de RSF version 2009
 * @author delabre
 *
 */
public class PmsiRsf2009h extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(H)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(.{8})(.{7})(.{5})(.{7})" +
			"(.{7})(.{7})(.{3})(.{7})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire", "NumFacture",
		"DateDebutSejour", "CodeUCD", "CoefficientFractionnement", "PrixAchatUnitaire", "MontantUnitaireEcartIndemnisable",
		"MontantTotalEcartIndemnisable", "Quantite", "MontantTotalFactureTTC"
	};

	private static final String name = "RsfH";

	private static final String[][] transforms = {
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{"(\\d{2})(\\d{2})(\\d{4})", "$3-$2-$1"},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null}
	};

	public PmsiRsf2009h() {
		super(name, pattern, names, transforms);
	}
	
}
