package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.regex.Pattern;

/**
 * Définition d'une ligne L de RSF version 2012
 * @author delabre
 *
 */
public class PmsiRsf2012i extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(I)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(\\d{8})(\\d{8})(.{1})(.{14})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire", "NumFacture",
		"DateDebutSejour", "DateFinSejour", "NatureFinSejour", "LieuExecutionActe"
	};

	private static final String name = "Rsf!i";

	private static final String[][] transforms = {
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
		{null, null}
	};
	
	public PmsiRsf2012i() {
		super(name, pattern, names, transforms);
	}
	
}
