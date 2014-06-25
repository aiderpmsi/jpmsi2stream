package com.github.aiderpmsi.pims.parser.linestypes;

public class LineBuilder {

	private LineConfDictionary dico;
	
	public LineBuilder(LineConfDictionary dico) {
		this.dico = dico;
	}
	
	public IPmsiLine createLine(String type) {
		if (type.equals("eof")) {
			return new EndOfFilePmsiLine();
		} else if (type.equals("linenumber")) {
			return new LineNumberPmsiLine();
		} else {
			return new ConfiguredPmsiLine(dico.getLineConf(type));
		}
	}

}
