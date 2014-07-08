package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class LineNumberPmsiLine implements IPmsiLine {

	private final ArrayList<Element> elements = new ArrayList<>(1);

	public Segment matchedSegment = null;
	
	public LineNumberPmsiLine() {
		elements.add(new Element("linenumber", new Segment(new char[] {'0'}, 0, 1)));
	}
	
	@Override
	public boolean matches(final Segment line) throws IOException {
		matchedSegment = line;
		return true;
	}

	@Override
	public int getLineSize() {
		return 0;
	}

	@Override
	public String getName() {
		return "linenumber";
	}

	@Override
	public Segment getMatchedLine() {
		return matchedSegment;
	}

	@Override
	public Collection<Element> getElements() {
		return elements;
	}

	public void setLineNumber(final Long number) {
		final String numberToString = number.toString();
		elements.set(0, new Element("linenumber", new Segment(numberToString.toCharArray(), 0, numberToString.length())));
	}

	@Override
	public String getVersion() {
		return "lineNumber";
	}
	
	public String getLine() {
		return elements.get(0).getElement().toString();
	}

}
