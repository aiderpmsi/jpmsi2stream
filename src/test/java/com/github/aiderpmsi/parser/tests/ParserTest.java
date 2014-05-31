package com.github.aiderpmsi.parser.tests;

import java.io.StringReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

import com.github.aiderpmsi.pims.parser.utils.Parser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;

public class ParserTest {
 
	private Parser p;
	
	public class TestEh implements ErrorHandler {

		public int numerrors = 0;
		
		@Override
		public void warning(SAXParseException exception) throws SAXException {
			numerrors++;
		}
		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
			numerrors++;
		}
		@Override
		public void error(SAXParseException exception) throws SAXException {
			numerrors++;
		}

	}
	
    @Before
    public void setUp() throws TreeBrowserException {
    	p = new Parser();
   }
 
    @After
    public void tearDown() {
    	// DO NOTHING
    }
 
    
    
    @Test
    public void testrss() throws Exception {
    	String rss = 
    			"123456789001610101201331122013000477006346008902720988560\n"
    			+ "1328Z04Z 116000123456789016131113              302000195           142843    27071949188  0 010220138 280220138 30450000000        120000000Z491    N189    0000                                \n"
    			+ "1328Z04Z 116000123456789016131709              302000259           150903    150819521RES1  040320138 040320138 30110000000        010000001Z491            000                                 04032013JVJF00801       01\n"
    			+ "1328Z04Z 116000123456789016131711              302000261           150905    150819521RES1  080320138 080320138 30110000000        010000001Z491            000                                 08032013JVJF00801       01";

    	p.setContentHandler(new DefaultHandler2());
    	TestEh eh = new TestEh();
    	p.setErrorHandler(eh);
    	p.setType("rssheader");
    	p.parse(new InputSource(new StringReader(rss)));
    	
    	Assert.assertEquals(0, eh.numerrors);
    	
    }
}