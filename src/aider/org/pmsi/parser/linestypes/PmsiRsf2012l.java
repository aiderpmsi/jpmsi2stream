package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

public class PmsiRsf2012l extends PmsiLineType {

	private static final Pattern pattern = Pattern.compile("^(L)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(.{2})(.{3})(.{8})(.{2})(.{8})" +
			"(.{8})(.{2})(.{8})(.{8})(.{2})(.{8})(.{8})(.{2})(.{8})(.{8})(.{2})(.{8})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire", "NumFacture",
		"ModeTraitement", "Discipline", "DateActe1", "QuantiteActe1", "CodeActe1", "DateActe2",
		"QuantiteActe2", "CodeActe2", "DateActe3", "QuantiteActe3", "CodeActe3", "DateActe4",
		"QuantiteActe4", "CodeActe4", "DateActe5", "QuantiteActe5", "CodeActe5"
	};

	private static final String name = "RsfL";

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
		return content;
	}
}
