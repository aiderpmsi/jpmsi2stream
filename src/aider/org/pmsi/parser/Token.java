package aider.org.pmsi.parser;

public class Token<D, T> {

	private D tokenName = null;
	
	private T tokenContent = null;

	public D getTokenName() {
		return tokenName;
	}

	public void setTokenName(D tokenName) {
		this.tokenName = tokenName;
	}

	public T getTokenContent() {
		return tokenContent;
	}

	public void setTokenContent(T tokenContent) {
		this.tokenContent = tokenContent;
	}
	
		
}
