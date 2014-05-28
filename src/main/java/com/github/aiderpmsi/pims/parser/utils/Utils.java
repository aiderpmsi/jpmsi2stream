package com.github.aiderpmsi.pims.parser.utils;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.Attributes2Impl;

public class Utils {

	public void noLineError(String error, Integer numLine, ErrorHandler erh) throws SAXException {
		erh.error(new SAXParseException(error + " were attended but not found", "pimsparser", "pimsparser", numLine, 0));
	}
	
	public void noHeaderError(Integer numLine, ErrorHandler erh)  throws SAXException {
		erh.error(new SAXParseException("No header found", "pimsparser", "pimsparser", numLine, 0));
	}
	
	public void writelinenumber(Integer lineNumber, ContentHandler ch) throws SAXException {
		ch.startElement("", "numline", "numline", new Attributes2Impl());
		String lineNumberString = lineNumber.toString();
		ch.characters(lineNumberString.toCharArray(), 0, lineNumberString.length());
		ch.endElement("",  "numline", "numline");
	}
	
}
