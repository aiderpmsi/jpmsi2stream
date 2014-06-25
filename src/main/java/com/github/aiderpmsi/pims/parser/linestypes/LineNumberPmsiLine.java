package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.LinkedHashMap;

public class LineNumberPmsiLine implements IPmsiLine {

	private LinkedHashMap<String, Segment> content;
	
	public LineNumberPmsiLine() {
		content = new LinkedHashMap<>();
	}
	
	@Override
	public boolean matches(Segment line) throws IOException {
		return true;
	}

	@Override
	public int getLineSize() {
		return 0;
	}

	@Override
	public LinkedHashMap<String, Segment> getResults() throws IOException {
		return content;
	}

	public void setLineNumber(Long number) {
		String numberS = number.toString();
		content.put("linenumber", new Segment(numberS.toCharArray(), 0, numberS.length()));
	}

	@Override
	public String getName() {
		return "linenumber";
	}
}
