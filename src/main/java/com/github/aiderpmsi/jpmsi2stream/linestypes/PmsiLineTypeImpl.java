package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.jpmi2stream.MemoryBufferedReader;

/**
 * Défini l'architecture pour créer des patrons de lignes pmsi avec :
 * <ul>
 *  <li>Les noms des éléments</li>
 *  <li>Les regex des éléments</li>
 *  <li>Les contenus des éléments</li>
 * </ul>
 * @author delabre
 *
 */
public class PmsiLineTypeImpl implements PmsiLineType {

	private Pattern pattern;
	
	private String[] names;
	
	private String[][] transforms;
	
	private String name;
	
	private String[] content;
	
	public PmsiLineTypeImpl(String name, Pattern pattern, String[] names, String[][] transforms) {
		this.name = name;
		this.pattern = pattern;
		this.names = names;
		this.transforms = transforms;
		this.content = new String[names.length];
	}
	
	protected Pattern getPattern() {
		return pattern;
	}
	
	protected String[] getNames() {
		return names;
	}
	
	protected String getName() {
		return name;
	}
	
	protected void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	
	public void writeResults(ContentHandler contentHandler) throws IOException {
		
		try {
			contentHandler.startElement("", getName(), getName(), null);
			
			for (int i = 0 ; i < names.length ; i++) {
				// Début d'élément :
				contentHandler.startElement("", names[i], names[i], null);
				
				// Contenu de l'élément
				if (transforms[i][0] == null)
					// Pas de transformation
					contentHandler.characters(content[i].toCharArray(),
							0, content[i].length());
				else {
					// Transformation
					String modContent = content[i].replaceFirst(transforms[i][0], transforms[i][1]);
					contentHandler.characters(modContent.toCharArray(),
							0, content[i].length());
				}
				
				// Fin de l'élément
				contentHandler.endElement("", names[i], names[i]);
			}

			contentHandler.endElement("", getName(), getName());
			
		} catch (SAXException se) {
			throw new IOException(se);
		}
	}

	@Override
	public boolean isFound(MemoryBufferedReader br) throws IOException {
		// Récupération de la ligne à lire
		String toParse = br.getLine();
		
		// Test du match
		Matcher match = pattern.matcher(toParse);

		// On a une ligne qui correspond
		if (match.lookingAt()) {
			for (int i = 0 ; i < match.groupCount() ; i++) {
				content[i] = match.group(i + 1);
			}
			
			// On supprime du reader ce qui a été lu.
			br.consume(match.end());
			
			return true;
		}
		// La ligne ne correspond pas
		else 
			return false;
	}
}
