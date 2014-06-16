package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;

import com.github.aiderpmsi.pims.parser.linestypes.LineDictionary;
import com.github.aiderpmsi.pims.parser.linestypes.PmsiLineType;
import com.github.aiderpmsi.pims.parser.linestypes.PmsiLineTypeImpl;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;

public class Parser2 extends XMLFilterImpl {

	private String start;
	
	public Parser2(String start) throws TreeBrowserException {
		this.start = start;
	}
	
	@Override
	public void parse(InputSource input) throws SAXException, IOException {
		// THIS WORKS ONLY ON CHARACTER STREAMS
		if (input.getCharacterStream() == null)
			throw new IOException("No CharacterStream in input");

		// START DOCUMENT
		getContentHandler().startDocument();
		getContentHandler().startElement("", "root", "root", new Attributes2Impl());
		
		try {
			// DEFINITIONS
			long linenumber = 1;
			LineDictionary dico = new LineDictionary();
			MemoryBufferedReader br = new MemoryBufferedReader(input.getCharacterStream());
			Utils utils = new Utils();
			
			PmsiLineType rsf2012header = null, rss116header = null;
			
			if (!start.equals("rsfheader") && !start.equals("rssheader")) {
				if ((rsf2012header = dico.getLine("rsf2012header")).isFound(br)) {
					start = "rsfheader";
				}
				else if ((rss116header = dico.getLine("rss116header")).isFound(br)) {
					start = "rssheader";
				}
				else {
					// NO HEADER FOUND
					utils.noHeaderError(linenumber, getErrorHandler());
					return;
				}
			}
			
			if (start.equals("rsfheader")) {
				// IF RSF2102HEADER WAS NOT ALREADY FOUND IN INITCHOICE, TEST IT NOW
				if (rsf2012header == null) {
					if (!(rsf2012header = dico.getLine("rsf2012header")).isFound(br)) {
						rsf2012header = null;
					}
				}
				
				// IF RSF2012HEADER WAS FOUND, TEST LINES
				if (rsf2012header != null) {
					PmsiLineType rsf2012a = dico.getLine("rsf2012a"),
							rsf2012b = dico.getLine("rsf2012b"),
							rsf2012c = dico.getLine("rsf2012c"),
							rsf2012h = dico.getLine("rsf2012h"),
							rsf2012i = dico.getLine("rsf2012i"),
							rsf2012l = dico.getLine("rsf2012l"),
							rsf2012m = dico.getLine("rsf2012m"),
							eof = dico.getLine("eof");
					
					utils.writelinenumber(linenumber, getContentHandler());
					linenumber++;
					rsf2012header.writeResults(getContentHandler());
					
					while (true) {
						utils.writelinenumber(linenumber, getContentHandler());
						linenumber++;
						if (rsf2012a.isFound(br)) {
							rsf2012a.writeResults(getContentHandler());
						} else if (rsf2012b.isFound(br)) {
							rsf2012b.writeResults(getContentHandler());
						} else if (rsf2012c.isFound(br)) {
							rsf2012c.writeResults(getContentHandler());
						} else if (rsf2012h.isFound(br)) {
							rsf2012h.writeResults(getContentHandler());
						} else if (rsf2012i.isFound(br)) {
							rsf2012i.writeResults(getContentHandler());
						} else if (rsf2012l.isFound(br)) {
							rsf2012l.writeResults(getContentHandler());
						} else if (rsf2012m.isFound(br)) {
							rsf2012m.writeResults(getContentHandler());
						} else if (eof.isFound(br)) {
							eof.writeResults(getContentHandler());
							return;
						} else {
							utils.noLineError("rsfa, rsfb, rsfc, rsfh, rsfi, rsfl, rsfm", linenumber, getErrorHandler());
							return;
						}
					}
				} else {
					// NO RSF HEADER WAS FOUND
					utils.noHeaderError(linenumber, getErrorHandler());
					return;
				}
				
			} else if (start.equals("rssheader")) {
				// IF RSS116HEADER WAS NOT ALREADY FOUND IN INITCHOICE, TEST IT NOW
				if (rss116header == null) {
					if (!(rss116header = dico.getLine("rss116header")).isFound(br)) {
						rss116header = null;
					}
				}
	
				// IF RSS116HEADER WAS FOUND, TEST LINES
				if (rss116header != null) {
					PmsiLineTypeImpl rss116main = (PmsiLineTypeImpl) dico.getLine("rss116main");
					PmsiLineType rss116da = dico.getLine("rss116da"),
							rss116dad = dico.getLine("rss116dad"),
							rss116za = dico.getLine("rss116za"),
							eof = dico.getLine("eof");
	
					int nbda, nbdad, nbza, nbdaread, nbdadread, nbzaread;
					
					utils.writelinenumber(linenumber, getContentHandler());
					linenumber++;
					rss116header.writeResults(getContentHandler());
					
					while (true) {
						if (rss116main.isFound(br)) {
							utils.writelinenumber(linenumber, getContentHandler());
							linenumber++;
	
							rss116main.writeResults(getContentHandler());
							nbda = rss116main.getInt(26);
							nbdad = rss116main.getInt(27);
							nbza = rss116main.getInt(28);
							nbdaread = 0;
							nbdadread = 0;
							nbzaread = 0;
						
							while (nbdaread < nbda) {
								if (rss116da.isFound(br)) {
									rss116da.writeResults(getContentHandler());
									nbdaread++;
									continue;
								} else {
									utils.noLineError("da element", linenumber, getErrorHandler());
									return;
								}
							}
	
							while (nbdadread < nbdad) {
								if (rss116dad.isFound(br)) {
									rss116dad.writeResults(getContentHandler());
									nbdadread++;
									continue;
								} else {
									utils.noLineError("dad element", linenumber, getErrorHandler());
									return;
								}
							}
							
							while (nbzaread < nbza) {
								if (rss116za.isFound(br)) {
									rss116za.writeResults(getContentHandler());
									nbzaread++;
									continue;
								} else {
									utils.noLineError("za element", linenumber, getErrorHandler());
									return;
								}
							}
						}
	
						else if (eof.isFound(br)) {
							eof.writeResults(getContentHandler());
							return;
						} else {
							// RSSMAIN NOT FOUND
							utils.noLineError("rss116main element", linenumber, getErrorHandler());
						}
					}
					
				} else {
					// NO RSS HEADER FOUND
					utils.noHeaderError(linenumber, getErrorHandler());
					return;
				}
			}
		}
		
		finally {
			// END DOCUMENT
			getContentHandler().endElement("", "root", "root");
			getContentHandler().endDocument();
		}
	}
	

}
