package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class EndOfFilePmsiLine implements IPmsiLine {

	private final ArrayList<Element> elements = new ArrayList<>(1);

	private Segment segment = null;
	
	public EndOfFilePmsiLine() {
		elements.add(new Element("eof", new Segment(new char[] {'t',  'r',  'u', 'e'}, 0, 4)));
	}

	@Override
	public boolean matches(final Segment line) throws IOException {
		return true;
	}

	@Override
	public int getLineSize() {
		return 0;
	}

	@Override
	public String getName() {
		return "eof";
	}

	@Override
	public Segment getMatchedLine() {
		return segment;
	}

	@Override
	public Collection<Element> getElements() {
		return elements;
	}

}
