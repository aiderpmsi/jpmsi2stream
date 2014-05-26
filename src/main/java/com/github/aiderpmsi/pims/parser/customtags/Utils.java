package com.github.aiderpmsi.pims.parser.customtags;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.Attributes2Impl;

import com.github.aiderpmsi.pims.parser.linestypes.LineDictionary;
import com.github.aiderpmsi.pims.parser.linestypes.PmsiLineType;
import com.github.aiderpmsi.pims.parser.utils.MemoryBufferedReader;

public class Utils {

	private ErrorHandler erh;
	
	private MemoryBufferedReader br;
	
	private LineDictionary ld;
	
	private ContentHandler ch;

	public Utils(MemoryBufferedReader br, LineDictionary ld, ErrorHandler erh, ContentHandler ch) {
		this.br = br;
		this.ld = ld;
		this.erh = erh;
		this.ch = ch;
	}
	
	public PmsiLineType getLine(String lineName) throws IOException {
		// GETS THE LINE DEFINITION
		return ld.getLine(lineName);
	}

	public Boolean isLine(String lineName) throws IOException {
		// GETS THE LINE DEFINITION
		PmsiLineType line = ld.getLine(lineName);

		// CHECKS IF THE ACTUAL LINE CORRESPONS TO THIS LINE
		return line.isFound(br);
	}

	public void error(String error, Integer numLine) throws SAXException {
		erh.error(new SAXParseException(error, "scxml", "scxml", numLine, 0));
	}
	
	public void write(String linename) throws IOException {
		// GETS THE LINE DEFINITION
		PmsiLineType line = ld.getLine(linename);

		// WRITES THE RESULT IN THE CONTENTHANDLER
		line.writeResults(ch);
		
		// REMOVES THE CORRESPNDING DATAS IN LINEREADER
		line.consume(br);
	}
	
	public void writelinenumber(Integer lineNumber) throws SAXException {
		ch.startElement("", "numline", "numline", new Attributes2Impl());
		String lineNumberString = lineNumber.toString();
		ch.characters(lineNumberString.toCharArray(), 0, lineNumberString.length());
		ch.endElement("",  "numline", "numline");
	}

	public void print(String content, Boolean newLine) throws SAXException {
		ch.characters(content.toCharArray(), 0, content.length());
		if (newLine)
			ch.characters("\n".toCharArray(), 0, 1);
	}
	
}
