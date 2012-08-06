package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie principale d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Main extends PmsiLineType {

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

	private String[] content = new String[names.length];

	public Pattern getPattern() {
		return pattern;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public String getName() {
		return name;
	}

	public void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	public String[] getContent() {
		return content;
	}
}
