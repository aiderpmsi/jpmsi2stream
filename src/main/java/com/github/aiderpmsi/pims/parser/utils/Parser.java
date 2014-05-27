package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;

import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserFactory;

public class Parser extends XMLFilterImpl {

	private TreeBrowserFactory<ParserConfig> tbf;

	private String type = "";
	
	public Parser() throws TreeBrowserException {
		ParserConfigBuilder gcb = new ParserConfigBuilder();
		this.tbf = new TreeBrowserFactory<>(gcb);
	}
	
	@Override
	public void parse(InputSource input) throws SAXException, IOException {
		// THIS WORKS ONLY ON CHARACTER STREAMS
		if (input.getCharacterStream() == null)
			throw new IOException("No CharacterStream in input");

		// GETS THE DOCUMENT
		TreeBrowser tb;
		try {
			tb = tbf.build();

			// SETS THE PARAMETERS
			tb.getJc().set("br", new MemoryBufferedReader(input.getCharacterStream()));
			tb.getJc().set("ch", getContentHandler());
			tb.getJc().set("eh", getErrorHandler());
			tb.getJc().set("start", getType());
			
			getContentHandler().startDocument();
			getContentHandler().startElement("", "root", "root", new Attributes2Impl());
	
			// LAUNCHES THE PARSER
			tb.go();
			
			getContentHandler().endElement("", "root", "root");
			getContentHandler().endDocument();
		} catch (TreeBrowserException e) {
			throw new SAXException(e);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
