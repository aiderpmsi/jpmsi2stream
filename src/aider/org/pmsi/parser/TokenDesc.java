package aider.org.pmsi.parser;

import java.util.regex.Pattern;

public class TokenDesc<T> {

	private T tokenIdentification = null;
	
	private Pattern matchPattern = null;

	public TokenDesc (T tokenIdentification, String pattern) {
		setTokenIdentification(tokenIdentification);
		setMatchPattern(pattern);
	}
	
	public T getTokenIdentification() {
		return tokenIdentification;
	}

	public void setTokenIdentification(T tokenIdentification) {
		this.tokenIdentification = tokenIdentification;
	}

	public Pattern getMatchPattern() {
		return matchPattern;
	}

	public void setMatchPattern(String pattern) {
		this.matchPattern = Pattern.compile(pattern);
	}
	
	
}
