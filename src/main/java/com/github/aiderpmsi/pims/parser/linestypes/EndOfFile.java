package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;

import com.github.aiderpmsi.pims.parser.utils.MemoryBufferedReader;

public class EndOfFile extends PmsiLineType {

	public EndOfFile(LineWriter lineWriter) {
		super(lineWriter);
	}

	public boolean isFound(MemoryBufferedReader br) throws IOException {
		if (br.getLine() == null)
			return true;
		else
			return false;
	}

}
