package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;

import com.github.aiderpmsi.pims.parser.linestypes.IPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.LineBuilder;
import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.parser.utils.Utils.LineHandler;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treemodel.Node;

public class XmlParser extends XMLFilterImpl {

	private final Node<?> tree;

	private final String type;
	
	private final LineConfDictionary dico;

	private final XmlLineHandler xmlLineHandler;
	
	private final XmlErrorHandler xmlErrorHandler;
		
	public XmlParser(
			final Node<?> tree,
			final LineConfDictionary dico,
			final String type,
			final XmlLineHandler xmlLineHandler,
			final XmlErrorHandler xmlErrorHandler) throws TreeBrowserException {
		this.tree = tree;
		this.type = type;
		this.dico = dico;
		this.xmlLineHandler = xmlLineHandler;
		this.xmlErrorHandler = xmlErrorHandler;
	}

	@Override
	public void parse(final InputSource input) throws SAXException, IOException {

		// THIS WORKS ONLY ON CHARACTER STREAMS
		if (input.getCharacterStream() == null)
			throw new IOException("No CharacterStream on input");

		// CREATES THE MEMORYBUFFERED READER (KEEPS IN MEMORY THE LAST READING LINE AND CONSUMES ONLY EXPLICITELY CHARACTERS)
		final PimsParserFromReader mbr = new PimsParserFromReader(input.getCharacterStream());

		// SETS THE CONTEXT VARS
		final HashMap<String, Object> context = new HashMap<>(4);
		context.put("lb", new LineBuilder(dico));
		context.put("br", mbr);
		LineHandler lh = (pmsiLine) -> xmlLineHandler.handle(pmsiLine, getContentHandler());
		context.put("utils", new Utils(mbr,
				Arrays.asList(lh),
				(msg, line) -> xmlErrorHandler.handle(msg, line, getErrorHandler())));
		context.put("start", type);

		// CREATES THE TREE BROWSER
		final TreeBrowser tb = new TreeBrowser(tree, context);

		try {
			getContentHandler().startDocument();

			getContentHandler().startElement("", "root", "root", new Attributes2Impl());

			// EXECUTES THE TREE BROWSER
			try {
				tb.go();
			} catch (Exception e) {
				// BE SURE TO CATCH EVERY EXCEPTION FROM MACHINE
				throw new IOException(e);
			} finally {
				getContentHandler().endElement("", "root", "root");
			}
			
		} finally {
			// ALWAYS BE SURE TO SEND END DOCUMENT (CAN CLOSE STREAMS, ...)
			getContentHandler().endDocument();
		}
	}

	public String getType() {
		return type;
	}

	public interface XmlLineHandler {
		public void handle(final IPmsiLine pmsiLine, final ContentHandler ch) throws IOException;
	}
	
	public interface XmlErrorHandler {
		public void handle(final String msg, final long lineNumber, final ErrorHandler eh) throws IOException;
	}
}


