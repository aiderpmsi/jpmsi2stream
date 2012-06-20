package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

public class PmsiRss116Dad extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(.{8})");
	
	private static final String[] names = {
		"DAD"
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
