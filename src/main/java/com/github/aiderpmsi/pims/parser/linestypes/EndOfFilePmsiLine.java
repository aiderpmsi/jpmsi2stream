package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.LinkedHashMap;

public class EndOfFilePmsiLine implements IPmsiLine {

	private LinkedHashMap<String, Segment> content;
	
	public EndOfFilePmsiLine() {
		content = new LinkedHashMap<>();
		content.put("eof", new Segment(new char[] {'t',  'r',  'u', 'e'}, 0, 4));
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

	@Override
	public String getName() {
		return "eof";
	}

}
