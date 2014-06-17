package com.github.aiderpmsi.pims.parser.linestypes;

public class LineBuilder {

	private LineConfDictionary dico;
	
	public LineBuilder(LineConfDictionary dico) {
		this.dico = dico;
	}
	
	public PmsiLineType createLine(String type) {
		if (type.equals("eof")) {
			return new EndOfFile();
		} else {
			return new PmsiLineTypeImpl(dico.getLineConf(type));
		}
	}

}
