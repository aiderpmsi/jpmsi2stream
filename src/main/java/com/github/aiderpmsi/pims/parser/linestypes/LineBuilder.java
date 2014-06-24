package com.github.aiderpmsi.pims.parser.linestypes;

import com.github.aiderpmsi.pims.parser.linestypes.PmsiLineType.LineWriter;

public class LineBuilder {

	private LineConfDictionary dico;
	
	private LineWriter lineWriter;
	
	public LineBuilder(LineConfDictionary dico, LineWriter lineWriter) {
		this.dico = dico;
		this.lineWriter = lineWriter;
	}
	
	public PmsiLineType createLine(String type) {
		if (type.equals("eof")) {
			return new EndOfFile(lineWriter);
		} else {
			return new PmsiLineTypeImpl(lineWriter, dico.getLineConf(type));
		}
	}

}
