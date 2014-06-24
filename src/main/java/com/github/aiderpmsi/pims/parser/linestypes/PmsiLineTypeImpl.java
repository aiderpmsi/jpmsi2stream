package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.ArrayList;

import com.github.aiderpmsi.pims.parser.model.Element;
import com.github.aiderpmsi.pims.parser.model.LineTypeDefinition;
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

	private ArrayList<PmsiElement> elements;

	private String name;
	
	private MemoryBufferedReader br = null;
	
	private String readedLine = null;
	
	private int matchLength;
	
	private String lineversion;
	
	public PmsiLineTypeImpl(LineWriter lineWriter, LineTypeDefinition linetype) {
		super(lineWriter);
		
		this.elements = new ArrayList<>(linetype.elements.size());
		this.name = linetype.name;
		this.lineversion = linetype.version;
		this.matchLength = 0;
		
		for (Element config : linetype.elements) {
			PmsiElement element;
			if (config.type.equals("int")) {
				element = new PmsiIntElement(config);
			} else if (config.type.equals("text")) {
				element = new PmsiTextElement(config);
			} else if (config.type.startsWith("fixed,")) {
				element = new PmsiFixedElement(config);
			} else if (config.type.startsWith("regexp,"))  {
				element = new PmsiRegexpElement(config);
			} else if (config.type.equals("date"))  {
				element = new PmsiDateElement(config);
			} else {
				throw new RuntimeException(config.type + "  type is unknown in " + getClass().getSimpleName());
			}
			elements.add(element);
			matchLength += element.getSize();
		}
	}
	
	@Override
	public boolean isFound(MemoryBufferedReader br) throws IOException {
		this.br = br;
		
		// Récupération de la ligne à lire
		String toParse = br.getLine();
		
		if (toParse == null || toParse.length() < matchLength)
			return false;

		// RECUPERATION DE LA PART DE LIGNE
		char[] array = new char[matchLength];
		toParse.getChars(0, matchLength, array, 0);
		
		// TENTATIVE DE MATCH
		int readPosition = 0;
		for (PmsiElement element : elements) {
			int endPosition = readPosition + element.getSize();
			element.setContent(new Segment(array, readPosition, element.getSize()));
			// IF READED ELEMENT DOESN'T CORRESPOND, CLEAR THE LINE AND RETURN FALSE
			if (!element.validate()) {
				readedLine = null;
				return false;
			}
			readPosition = endPosition;
		}
		// EVERY MATCH WORKED, STORES THE READED LINE AS FULL LINE
		readedLine = new String(array);
		return true;
	}
	
	public int getInt(int index) {
		Segment segt = elements.get(index).getContent();
		String num = new String(segt.sequence, segt.start, segt.count);
		return Integer.parseInt(num);
	}

	public String getName() {
		return name;
	}

	public String getReadedLine() {
		return readedLine;
	}

	public int getMatchLength() {
		return matchLength;
	}

	public String getLineversion() {
		return lineversion;
	}

	public ArrayList<PmsiElement> getElements() {
		return elements;
	}

	public MemoryBufferedReader getBr() {
		return br;
	}
	
}
