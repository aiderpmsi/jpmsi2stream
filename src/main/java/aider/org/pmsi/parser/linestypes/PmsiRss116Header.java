package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Capture l'entête d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class PmsiRss116Header extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(\\d{9})(\\d{3})(.{2})(\\d{8})(\\d{8})(\\d{6})(\\d{6})(\\d{7})(\\d{7})(.{1})");
			
	private static final String[] names = {
		"Finess", "NumLot", "StatutEtablissement", "DbtPeriode", "FinPeriode", "NbEnregistrements",
		"NbRSS", "PremierRSS", "DernierRSS", "DernierEnvoiTrimestre"
	};
	
	private static final String[][] transforms = {
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
	
	private static final String name = "RssHeader";

	public PmsiRss116Header() {
		super(name, pattern, names, transforms);
	}
	
}
