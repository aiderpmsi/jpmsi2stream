package com.github.aiderpmsi.parser.tests;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.ext.DefaultHandler2;

import com.github.aiderpmsi.pims.parser.linestypes.ConfiguredPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.IPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.IPmsiLine.Element;
import com.github.aiderpmsi.pims.parser.utils.XmlParser;
import com.github.aiderpmsi.pims.parser.utils.XmlParserFactory;
import com.github.aiderpmsi.pims.parser.utils.XmlParser.XmlLineHandler;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;

public class ParserTest {

	public class TestXmlLineHandler implements XmlLineHandler {

		@Override
		public void handle(final IPmsiLine pmsiLine, final ContentHandler ch) throws IOException {
			try {
				final Attributes2Impl attributes = new Attributes2Impl();
				if (pmsiLine instanceof ConfiguredPmsiLine) {
					attributes.addAttribute("", "version", "version", "text", ((ConfiguredPmsiLine) pmsiLine).getLineversion());
				}
				ch.startElement("", pmsiLine.getName(), pmsiLine.getName(), attributes);
				for (final Element element : pmsiLine.getElements()) {
					ch.startElement("", element.getName(), element.getName(), new Attributes2Impl());
					ch.characters(element.getElement().sequence, element.getElement().start, element.getElement().count);
					ch.endElement("", element.getName(), element.getName());
				}
			} catch (SAXException e) {
				throw new IOException(e);
			}
		}
	}
	
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
	
	public class TestCh implements ContentHandler {

		public List<String> elements = new ArrayList<>();
		
		public StringBuilder contentBuffer;
		
		public String lastLineNumber = null;
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (elements.get(elements.size() - 1).equals("linenumber")) {
				contentBuffer.append(ch, start, length);
			}
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (elements.get(elements.size() - 1).equals("linenumber")) {
				lastLineNumber = contentBuffer.toString();
			}
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		@Override
		public void processingInstruction(String target, String data)
				throws SAXException {
		}

		@Override
		public void setDocumentLocator(Locator locator) {
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			elements.add(localName);
			contentBuffer = new StringBuilder();
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
		}
		
	}
	
    @Before
    public void setUp() throws TreeBrowserException {
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

    	XmlParserFactory pf = new XmlParserFactory();
    	
    	XmlParser p = pf.newParser("rssHeader",
    			new TestXmlLineHandler(),
    			(msg, lineNumber, eh) -> {
    				try {
						eh.error(new SAXParseException(msg, "pims", "pims", (int)lineNumber, 0));
					} catch (SAXException e) {
						throw new IOException(e);
					}	
    			});
    	
    	TestCh ch = new TestCh();
    	p.setContentHandler(ch);
    	TestEh eh = new TestEh();
    	p.setErrorHandler(eh);

    	p.parse(new InputSource(new StringReader(rss)));

    	int mains = Collections.frequency(ch.elements, "rssmain");
    	int actes = Collections.frequency(ch.elements, "rssacte");
    	
    	Assert.assertEquals(0, eh.numerrors);
    	Assert.assertEquals(3, mains);
    	Assert.assertEquals(2, actes);
    	
    }
    
    @Test
    public void testrsf() throws Exception {
    	String rsf =
    			"78001731400160070101201331102013056024006430000000020988560"
    			+ "A7800173140                   22236119935340468000302130265110  41070211193610103201331032013003115550031155500026293000262930000000000000000000000000031155510         "
    			+ "B7800173140                   236119935340468000302130265197960203201302032013D11  001400100 0000000252680002526810000025268000252680000000000000025268PAI"
    			+ "B7800173140                   236119935340468000302130265197962303201323032013EMI  001400100 0000000014470000144710000001447000014470000000000000001447PAI"
    			+ "C7800173140                   23611993534046800030213026519796405032013CS   0100010001000230000023001000002300000230000000000002300PAI"
    			+ "C7800173140                   23611993534046800030213026519796405032013MPC  0100010001000020000002001000000200000020000000000000200PAI"
    			+ "A7800173140                   22236119935340468000302131038110  41070211193610104201330042013003368230033682300052478000524780000000000000000000000000033682310         "
    			+ "B7800173140                   236119935340468000302131038197962004201320042013PH8  001400100 0000000068920000689210000006892000068920000000000000006892PAI"
    			+ "B7800173140                   236119935340468000302131038197962004201320042013EMI  001400100 0000000014470000144710000001447000014470000000000000001447PAI"
    			+ "C7800173140                   23611993534046800030213103819796402042013CS   0100010001000230000023001000002300000230000000000002300PAI"
    			+ "C7800173140                   23611993534046800030213103819796402042013MPC  0100010001000020000002001000000200000020000000000000200PAI"
    			+ "A78001731409027               22253123022301667000302130029110  41012112195310101201331012013003293680032936800064833000648330000000000000000000000000032936810         "
    			+ "B78001731409027               253123022301667000302130029197963101201331012013D11  001400100 0000000253360002533610000025336000253360000000000000025336PAI"
    			+ "C78001731409027               25312302230166700030213002919796401012013CS   0100010001000230000023001000002300000230000000000002300PAI"
    			+ "C78001731409027               25312302230166700030213002919796431012013B    0100690004000002700018631000001863000186300000000001863PAI"
    			+ "L78001731409027               2531230223016670003021300291979603012013011104    03012013011109    03012013019005    03012013019105    0101190000        ";

    	XmlParserFactory pf = new XmlParserFactory();
    	
    	XmlParser p = pf.newParser("rsfHeader",
    	    			new TestXmlLineHandler(),
    	    			(msg, lineNumber, eh) -> {
    	    				try {
    							eh.error(new SAXParseException(msg, "pims", "pims", (int)lineNumber, 0));
    						} catch (SAXException e) {
    							throw new IOException(e);
    						}	
    	    			});
    	
    	TestCh ch = new TestCh();
    	p.setContentHandler(ch);
    	TestEh eh = new TestEh();
    	p.setErrorHandler(eh);

    	p.parse(new InputSource(new StringReader(rsf)));

    	Assert.assertEquals(0, eh.numerrors);
    	Assert.assertEquals(3, Collections.frequency(ch.elements, "rsfa"));
    	Assert.assertEquals(5, Collections.frequency(ch.elements, "rsfb"));
    	Assert.assertEquals(6, Collections.frequency(ch.elements, "rsfc"));
    	Assert.assertEquals(1, Collections.frequency(ch.elements, "rsfl"));
    	Assert.assertEquals("17", ch.lastLineNumber);
    }

    @Test
    public void testMultiRsf() throws Exception {
    	String rsf =
    			"78001731400160070101201331102013056024006430000000020988560"
    			+ "A7800173140                   22236119935340468000302130265110  41070211193610103201331032013003115550031155500026293000262930000000000000000000000000031155510         "
    			+ "B7800173140                   236119935340468000302130265197960203201302032013D11  001400100 0000000252680002526810000025268000252680000000000000025268PAI"
    			+ "B7800173140                   236119935340468000302130265197962303201323032013EMI  001400100 0000000014470000144710000001447000014470000000000000001447PAI"
    			+ "C7800173140                   23611993534046800030213026519796405032013CS   0100010001000230000023001000002300000230000000000002300PAI"
    			+ "C7800173140                   23611993534046800030213026519796405032013MPC  0100010001000020000002001000000200000020000000000000200PAI"
    			+ "A7800173140                   22236119935340468000302131038110  41070211193610104201330042013003368230033682300052478000524780000000000000000000000000033682310         "
    			+ "B7800173140                   236119935340468000302131038197962004201320042013PH8  001400100 0000000068920000689210000006892000068920000000000000006892PAI"
    			+ "B7800173140                   236119935340468000302131038197962004201320042013EMI  001400100 0000000014470000144710000001447000014470000000000000001447PAI"
    			+ "C7800173140                   23611993534046800030213103819796402042013CS   0100010001000230000023001000002300000230000000000002300PAI"
    			+ "C7800173140                   23611993534046800030213103819796402042013MPC  0100010001000020000002001000000200000020000000000000200PAI"
    			+ "A78001731409027               22253123022301667000302130029110  41012112195310101201331012013003293680032936800064833000648330000000000000000000000000032936810         "
    			+ "B78001731409027               253123022301667000302130029197963101201331012013D11  001400100 0000000253360002533610000025336000253360000000000000025336PAI"
    			+ "C78001731409027               25312302230166700030213002919796401012013CS   0100010001000230000023001000002300000230000000000002300PAI"
    			+ "C78001731409027               25312302230166700030213002919796431012013B    0100690004000002700018631000001863000186300000000001863PAI"
    			+ "L78001731409027               2531230223016670003021300291979603012013011104    03012013011109    03012013019005    03012013019105    0101190000        ";

    	XmlParserFactory pf = new XmlParserFactory();
    	    	
    	DefaultHandler2 ch = new DefaultHandler2();
    	for (int i = 0 ; i < 10000 ; i++) {
    		XmlParser p = pf.newParser("rsfHeader",
    				(line, ch2) -> {},
	    			(msg, lineNumber, eh) -> {
	    				try {
							eh.error(new SAXParseException(msg, "pims", "pims", (int)lineNumber, 0));
						} catch (SAXException e) {
							throw new IOException(e);
						}	
	    			});
	
	    	p.setContentHandler(ch);
	    	p.setErrorHandler(ch);
	
	    	p.parse(new InputSource(new StringReader(rsf)));
    	}
    	
    }

}