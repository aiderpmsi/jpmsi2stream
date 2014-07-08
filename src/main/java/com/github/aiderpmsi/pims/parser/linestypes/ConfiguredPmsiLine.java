package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiDateElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiFixedElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiIntElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiRegexpElement;
import com.github.aiderpmsi.pims.parser.linestypes.elements.PmsiTextElement;
import com.github.aiderpmsi.pims.parser.model.LineTypeDefinition;
import com.github.aiderpmsi.pims.parser.model.PmsiElementConfig;

/**
 * @author delabre
 *
 */
public class ConfiguredPmsiLine implements IPmsiLine {

	private final ArrayList<Element> elements;

	private final ArrayList<PmsiElement> elementsConfig;
	
	private final String name;
	
	private final int matchLength;
	
	private final String lineversion;
	
	private Segment segment = null;
	
	public ConfiguredPmsiLine(final LineTypeDefinition linetype) {
		elements = new ArrayList<>(linetype.elements.size());
		elementsConfig = new ArrayList<>();
		lineversion = linetype.version;
		name = linetype.name;
		int calculatedMatchLength = 0;
		
		for (PmsiElementConfig config : linetype.elements) {
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
			elements.add(new Element(element.getName(), null));
			elementsConfig.add(element);
			calculatedMatchLength += element.getSize();
		}
		matchLength = calculatedMatchLength;
	}

	@Override
	public boolean matches(final Segment line) throws IOException {

		if (line == null || line.length() < matchLength)
			return false;
		else if (line.length() > matchLength) {
			throw new IOException("Implementation error : line bigger than awaited");
		}
		
		// RESETS THE MATCHED ELEMENTS AND THE MATCHED LINE
		elements.replaceAll((listElement) -> new Element(listElement.getName(), null));
		segment = line;

		// TRY TO MATCH
		int readPosition = line.start;
		int i = 0;
		for (final PmsiElement element : elementsConfig) {
			// SETS THE CONFIG ELEMENT IN ORDER TO SEE IF IT MATCHES
			element.setContent(new Segment(line.sequence, readPosition, element.getSize()));
			
			// IF READED ELEMENT DOESN'T CORRESPOND, CLEAR THE LINE AND RETURN FALSE
			if (!element.validate()) {
				elements.replaceAll((listElement) -> new Element(listElement.getName(), null));
				segment = null;
				return false;
			} else {
				elements.set(i++, new Element(element.getName(), element.getContent()));
				readPosition += element.getSize();
			}
		}
		return true;
	}

	@Override
	public int getLineSize() {
		return matchLength;
	}

	@Override
	public Segment getMatchedLine() {
		return segment;
	}

	@Override
	public Collection<Element> getElements() {
		return elements;
	}

	public String getName() {
		return name;
	}

	public String getLineversion() {
		return lineversion;
	}
	
}
