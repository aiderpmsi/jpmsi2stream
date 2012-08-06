package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie diagnostics documentaires d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Dad extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(.{8})");
	
	private static final String[] names = {
		"DAD"
	};
	
	private static final String name = "RssDad";

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
