package aider.org.pmsi.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class Rss116Tokenizer implements Tokenizer<RSS116Tokens, String> {
	
	private enum Rss116Step { Header, Main, Da, Dad, Acte };
	
	private Rss116Step rss116Step = Rss116Step.Header;
	
	private Integer NbDa = null;
	
	private Integer NbDad = null;
	
	private Integer NbActe = null;
	
	private CharBuffer charBuffer = CharBuffer.allocate(512);
	
	private Reader reader = null;
	
	private static final List<TokenDesc<RSS116Tokens>> rss116header =
			new ArrayList<TokenDesc<RSS116Tokens>>()
			{private static final long serialVersionUID = 5490194449944889878L;
			{add(new TokenDesc<RSS116Tokens>(RSS116Tokens.FinessHeader, "(\\d{9})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.NumLotHeader, "(\\d{3})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.StatutEtablissementHeader, "(.{2})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.DbtPeriodeHeader, "(\\d{8})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.FinPeriodeHeader, "(\\d{8})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.NbEnregistrementsHeader, "(\\d{6})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.NbRSSHeader, "(\\d{6})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.PremierRSSHeader, "(\\d{7})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.DernierRSSHeader, "(\\d{7})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.DernierEnvoiTrimestreHeader, "(.{1})"));
			 add(new TokenDesc<RSS116Tokens>(RSS116Tokens.NewLine, "(\\r?\\n)"));
			 }};
	
	private Iterator<TokenDesc<RSS116Tokens>> rss116HeaderIterator = null;
	
	public Rss116Tokenizer (Reader reader) {
		this.reader = reader;
		rss116HeaderIterator = rss116header.iterator();
	}
	
	@Override
	public Token<RSS116Tokens, String> read() throws IOException {
		switch(rss116Step) {
			case Header:
				if (rss116HeaderIterator.hasNext())
					return matchToken(rss116HeaderIterator.next());
				else {
					rss116Step = Rss116Step.Main;
				}
		}
	}
	
	private Token<RSS116Tokens, String> matchToken(TokenDesc<RSS116Tokens> tokenDesc) throws IOException {
		Matcher match = null;
		CharBuffer target = null;
		Token<RSS116Tokens, String> token = new Token<RSS116Tokens, String>();
		
		// Récupération de la chaine de caractères du token
		while (true) {
			match = tokenDesc.getMatchPattern().matcher(charBuffer);

			// En cas de match, on continue
			if (match.matches())
				break;
			
			// En cas d'absence de matching, on lit plus de caractères pour voir si on
			// y arrive
			CharBuffer tempBuffer = CharBuffer.allocate(512);
			// Si on ne peut ni matcher, ni lire de caractères, on a une erreur de parsing fatale
			if (reader.read(tempBuffer) == -1)
				throw new IOException("Token non trouvé");
			charBuffer.append(tempBuffer);
		}
		
		// Création du token
		token.setTokenName(tokenDesc.getTokenIdentification());
		target = CharBuffer.allocate(match.end());
		charBuffer.read(target);
		token.setTokenContent(target.toString());
		
		return token;
	}
}
