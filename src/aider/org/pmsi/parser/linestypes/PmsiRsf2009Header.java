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

	private static final String[][] transforms = {
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{"(\\d{4})(\\d{2})(\\d{2})", "$3-$2-$1"},
		{"(\\d{4})(\\d{2})(\\d{2})", "$3-$2-$1"},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		};
	
	private static final String name = "RsfHeader";
	
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
		String[] modContent = new String[names.length];
		for (int i = 0 ; i < names.length ; i++) {
			if (transforms[i][0] == null)
				modContent[i] = content[i];
			else {
				modContent[i] = content[i].replaceFirst(transforms[i][0], transforms[i][1]);
			}
		}
		return modContent;
	}
}
