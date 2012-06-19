package aider.org.pmsi.parser;

import java.io.IOException;

public interface Tokenizer<T, D> {

	public Token<T, D> read()  throws IOException;
	
}
