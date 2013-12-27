package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.regex.Pattern;

/**
 * Capture l'entête d'un fichier RSF 2009
 * @author delabre
 *
 */
public class PmsiRsf2009Header extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(\\d{9})(\\d{3})(.{2})(.{2})(\\d{8})(\\d{8})(\\d{6})(\\d{6})(\\d{7})(\\d{7})(.{1})");
	
	private static final String[] names = {
		"Finess", "NumLot", "StatutJuridique", "ModeTarifs", "DateDebut", "DateFin", "NbEnregistrements",
		"NbRSS", "PremierRSS", "DernierRSS", "DernierEnvoi"
	};

	private static final String[][] transforms = {
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
		};
	
	private static final String name = "RsfHeader";
	
	public PmsiRsf2009Header() {
		super(name, pattern, names, transforms);
	}

}
