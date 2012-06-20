package aider.org.pmsi.parser;

public class PmsiDefToken {

	private String name;
	
	private String regex;

	public PmsiDefToken (String name, String regex) {
		this.name = name;
		this.regex = regex;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
	
}
