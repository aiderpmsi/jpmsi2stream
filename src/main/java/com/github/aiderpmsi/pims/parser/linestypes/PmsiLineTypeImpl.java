package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import javax.swing.text.Segment;

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

	private PmsiElement[] elements;

	private String name;
	
	private MemoryBufferedReader br = null;
	
	int matchLength;
	
	String lineversion;
	
	public PmsiLineTypeImpl(Linetype linetype) {
		this.elements = new PmsiElement[linetype.elements.size()];
		this.name = linetype.name;
		this.lineversion = linetype.version;
		
		int i = 0;
		for (Element config : linetype.elements) {
			if (config.type.equals("int")) {
				elements[i] = new PmsiIntElement(config);
			} else if (config.type.equals("text")) {
				elements[i] = new PmsiTextElement(config);
			} else if (config.type.startsWith("fixed,")) {
				elements[i] = new PmsiFixedElement(config);
			} else if (config.type.startsWith("regexp,"))  {
				elements[i] = new PmsiRegexpElement(config);
			} else if (config.type.equals("date"))  {
				elements[i] = new PmsiDateElement(config);
			} else {
				throw new RuntimeException(config.type + "  type is unknown in " + getClass().getSimpleName());
				
			}
			matchLength += elements[i].getSize();
			i++;
		}
	}
	
	public void writeResults(ContentHandler contentHandler) throws IOException {
		
		try {
			// WRITES FIRST ELEMENT WITH VERSION IN ATTRIBUTES
			Attributes2Impl atts = new Attributes2Impl();
			atts.addAttribute("", "version", "version", "text", lineversion);
			contentHandler.startElement("", name, name, atts);
			
			for (int i = 0 ; i < elements.length ; i++) {
				// Début d'élément :
				contentHandler.startElement("", elements[i].getName(), elements[i].getName(), new Attributes2Impl());
				
				// Contenu de l'élément
				Segment content = elements[i].getContent();
				contentHandler.characters(content.array, content.offset, content.count);
				// Fin de l'élément
				contentHandler.endElement("", elements[i].getName(), elements[i].getName());
			}

			contentHandler.endElement("", name, name);
			
			// CONSUME IT FROM MEMORYBUFFEREDREADER
			br.consume(matchLength);
			
		} catch (SAXException se) {
			throw new IOException(se);
		}
	}

	@Override
	public boolean isFound(MemoryBufferedReader br) throws IOException {
		this.br = br;
		
		// Récupération de la ligne à lire
		String toParseS = br.getLine();
		
		if (toParseS == null)
			return false;
		
		char[] toParse = toParseS.toCharArray();
		// TENTATIVE DE MATCH
		int readed = 0;
		for (int i = 0 ; i < elements.length ; i++) {
			int toread = elements[i].getSize();
			if (readed + toread > toParse.length) {
				return false;
			} else {
				Segment segt = new Segment(toParse, readed, toread);
				elements[i].setContent(segt);
				if (elements[i].validate() != true) {
					return false;
				}
				readed += toread;
			}
		}
			
		// TOUS LES MATCH ONT MARCHE
		return true;
	}
	
	public int getInt(int index) {
		return Integer.parseInt(elements[index].getContent().toString());
	}

}
