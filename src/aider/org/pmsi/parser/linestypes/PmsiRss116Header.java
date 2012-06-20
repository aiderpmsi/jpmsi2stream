package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;


/**
 * Capture l'entête d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class PmsiRss116Header extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(\\d{9})(\\d{3})(.{2})(\\d{8})(\\d{8})(\\d{6})(\\d{6})(\\d{7})(\\d{7})(.{1})");
			
	private static final String[] names = {
		"Finess", "NumLot", "StatutEtablissement", "DbtPeriode", "FinPeriode", "NbEnregistrements",
		"NbRSS", "PremierRSS", "DernierRSS", "DernierEnvoiTrimestre"
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
