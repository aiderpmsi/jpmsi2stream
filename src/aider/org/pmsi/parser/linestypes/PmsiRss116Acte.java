package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

public class PmsiRss116Acte extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(\\d{8})(.{7})(.{1})(.{1})(.{1})(.{4})(.{1})(.{1})(\\d{2})");
	
	private static final String[] names = {
		"DateRealisation", "CodeCCAM", "Phase", "Activite", "ExtensionDoc", "Modificateurs",
		"RemboursementExceptionnel", "AssociationNonPrevue", "NbActes"
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
