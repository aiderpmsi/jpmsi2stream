package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;

import com.github.aiderpmsi.pims.parser.model.Element;
import com.github.aiderpmsi.pims.parser.model.Linetype;
import com.github.aiderpmsi.pims.parser.utils.MemoryBufferedReader;

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
public class PmsiLineTypeImpl extends PmsiLineType {

	private Pattern pattern;
	
	private String[] names;
	
	private String[][] transforms;
	
	private String name;
	
	private String[] content;
	
	private MemoryBufferedReader br = null;
	
	int matchLength = 0;
	
	public PmsiLineTypeImpl(Linetype linetype) {
		this.name = linetype.getName();
		this.names = new String[linetype.getElements().size()];
		this.transforms = new String[linetype.getElements().size()][2];
		this.content = new String[linetype.getElements().size()];
		StringBuilder patternS = new StringBuilder("^");

		int count = 0;
		for (Element element : linetype.getElements()) {
			this.names[count] = element.getName();
			if (element.getIn().length() == 0) {
				this.transforms[count][0] = null;
				this.transforms[count][1] = null;
			} else {
				this.transforms[count][0] = element.getIn();
				this.transforms[count][1] = element.getOut();
			}
			patternS.append("(").append(element.getPattern()).append(")");
			count++;
		}

		this.pattern = Pattern.compile(patternS.toString());
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
	
	protected String[] getContent() {
		return content;
	}

	protected void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	
	public void writeResults(ContentHandler contentHandler) throws IOException {
		
		try {
			contentHandler.startElement("", getName(), getName(), new Attributes2Impl());
			
			for (int i = 0 ; i < names.length ; i++) {
				// Début d'élément :
				contentHandler.startElement("", names[i], names[i], new Attributes2Impl());
				
				// Contenu de l'élément
				if (transforms == null || transforms[i][0] == null)
					// Pas de transformation
					contentHandler.characters(content[i].toCharArray(),
							0, content[i].length());
				else {
					// Transformation
					String modContent = content[i].replaceFirst(transforms[i][0], transforms[i][1]);
					contentHandler.characters(modContent.toCharArray(),
							0, modContent.length());
				}
				
				// Fin de l'élément
				contentHandler.endElement("", names[i], names[i]);
			}

			contentHandler.endElement("", getName(), getName());
			
			// CONSUME IT FROM MEMORYBUFFEREDREADER
			br.consume(matchLength);

			matchLength = 0;
			
		} catch (SAXException se) {
			throw new IOException(se);
		}
	}

	@Override
	public boolean isFound(MemoryBufferedReader br) throws IOException {
		this.br = br;
		
		// Récupération de la ligne à lire
		String toParse = br.getLine();
		
		if (toParse == null)
			return false;
		
		// Test du match
		Matcher match = pattern.matcher(toParse);

		// On a une ligne qui correspond
		if (match.lookingAt()) {
			for (int i = 0 ; i < match.groupCount() ; i++) {
				content[i] = match.group(i + 1);
			}
			
			// On Sauvegarde la taille de ce qui a été lu
			matchLength = match.end();
			
			return true;
		}
		// La ligne ne correspond pas
		else 
			return false;
	}
	
	public int getInt(int index) {
		return Integer.parseInt(content[index]);
	}

}
