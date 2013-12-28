package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie principale d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Main extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(.{2})(.{2})(.{4})(.{1})(116)(.{3})(.{9})(016)(.{20})(.{20})(.{10})" +
			"(\\d{8})([1|2])(.{4})(.{2})(\\d{8})(.{1})(.{1})(\\d{8})(.{1})(.{1})(.{5})(.{4})(.{2})(\\d{8}|[ ]{8})(.{2})(.{2})" +
			"(.{2})(.{3})(.{8})(.{8})(.{3})(.{1})(.{1})(.{1})(.{15})(.{15})");
	
	private static final String[] names = {
		"VersionClassification", "NumCMD", "NumGHM", "Filler", "VersionFormatRSS", "GroupageCodeRet", "Finess",
		"VersionFormatRUM", "NumRSS", "NumLocalSejour", "NumRUM", "DDN", "Sexe", "NumUniteMedicale", "TypeAutorisationLit",
		"DateEntree", "ModeEntree", "Provenance", "DateSortie", "ModeSortie", "Destination", "CPResidence", "PoidsNouveauNe",
		"AgeGestationnel", "DDRegles", "NbSeances", "NbDA", "NbDAD", "NbZA", "DP", "DR", 
		"IGS2", "ConfCodageRSS", "TypeMachineRadiotherapie", "TypeDosimetrie", "NumInnovation", "ZoneReservee"
	};

	private static final String name = "RssMain";

	public PmsiRss116Main() {
		super(name, pattern, names, null);
	}
	
	public int getnbda() {
		return Integer.decode(getContent()[26]);
	}

	public int getnbdad() {
		return Integer.decode(getContent()[27]);
	}
	
	public int getnbza() {
		return Integer.decode(getContent()[28]);
	}

}
