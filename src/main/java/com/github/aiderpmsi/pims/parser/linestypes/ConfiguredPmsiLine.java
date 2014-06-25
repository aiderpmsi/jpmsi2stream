package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiDateElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiFixedElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiIntElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiRegexpElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiTextElement;
import com.github.aiderpmsi.pims.parser.model.Element;
import com.github.aiderpmsi.pims.parser.model.LineTypeDefinition;

/**
 * @author delabre
 *
 */
public class ConfiguredPmsiLine implements IPmsiLine {

	private LinkedHashMap<String, Segment> contents = null;

	private ArrayList<PmsiElement> elements;
	
	private String name;
	
	private int matchLength;
	
	private String lineversion;
	
	public ConfiguredPmsiLine(LineTypeDefinition linetype) {
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
	public boolean matches(Segment line) throws IOException {

		if (line == null || line.length() < matchLength)
			return false;
		else if (line.length() > matchLength) {
			throw new IOException("Implementation error : liner bigger than awaited");
		}
		
		contents = new LinkedHashMap<>();

		// TENTATIVE DE MATCH
		int readPosition = line.start;
		for (PmsiElement element : elements) {
			int endPosition = readPosition + element.getSize();
			element.setContent(new Segment(line.sequence, readPosition, element.getSize()));
			// IF READED ELEMENT DOESN'T CORRESPOND, CLEAR THE LINE AND RETURN FALSE
			if (!element.validate()) {
				contents = null;
				return false;
			} else {
				readPosition = endPosition;
				contents.put(element.getName(), element.getContent());
			}
		}
		return true;
	}

	public String getName() {
		return name;
	}

	@Override
	public int getLineSize() {
		return matchLength;
	}

	public String getLineversion() {
		return lineversion;
	}

	public ArrayList<PmsiElement> getElements() {
		return elements;
	}

	@Override
	public LinkedHashMap<String, Segment> getResults() throws IOException {
		return contents;
	}
	
}
