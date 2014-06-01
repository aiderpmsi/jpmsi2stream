package com.github.aiderpmsi.pims.parser.utils;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.Attributes2Impl;

public class Utils {

	public void noLineError(String error, long numLine, ErrorHandler erh) throws SAXException {
		erh.error(new SAXParseException(error + " were attended but not found", "pimsparser", "pimsparser", (int) numLine, 0));
	}
	
	public void noHeaderError(long numLine, ErrorHandler erh)  throws SAXException {
		erh.error(new SAXParseException("No header found", "pimsparser", "pimsparser", (int) numLine, 0));
	}
	
	public void writelinenumber(long lineNumber, ContentHandler ch) throws SAXException {
		ch.startElement("", "numline", "numline", new Attributes2Impl());
		String lineNumberString = Long.toString(lineNumber);
		ch.characters(lineNumberString.toCharArray(), 0, lineNumberString.length());
		ch.endElement("",  "numline", "numline");
	}
	
}
