package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie Acte d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Acte extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(\\d{8})(.{7})(.{1})(.{1})(.{1})(.{4})(.{1})(.{1})(\\d{2})");
	
	private static final String[] names = {
		"DateRealisation", "CodeCCAM", "Phase", "Activite", "ExtensionDoc", "Modificateurs",
		"RemboursementExceptionnel", "AssociationNonPrevue", "NbActes"
	};

	private static final String name = "RssActe";

	private String[] content = new String[names.length];

	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public String[] getNames() {
		return names;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	@Override
	public String[] getContent() {
		return content;
	}
}
