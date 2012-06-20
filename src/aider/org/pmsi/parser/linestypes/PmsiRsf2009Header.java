package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Capture l'entête d'un fichier RSF
 * @author delabre
 *
 */
public class PmsiRsf2009Header extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(\\d{9})(\\d{3})(.{2})(.{2})(\\d{8})(\\d{8})(\\d{6})(\\d{6})(\\d{7})(\\d{7})(.{1})");
	
	private static final String[] names = {
		"Finess", "NumLot", "StatutJuridique", "ModeTarifs", "DateDebut", "DateFin", "NbEnregistrements",
		"NbRSS", "PremierRSS", "DernierRSS", "DernierEnvoi"
	};

	private String[] content = new String[names.length];

	public Pattern getPattern() {
		return pattern;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	public String[] getContent() {
		return content;
	}
}
