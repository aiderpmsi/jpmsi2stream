package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie Diagnostics associés d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Da extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(.{8})");
	
	private static final String[] names = {
		"DA"
	};
	
	private static final String name = "RssDa";
	
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
