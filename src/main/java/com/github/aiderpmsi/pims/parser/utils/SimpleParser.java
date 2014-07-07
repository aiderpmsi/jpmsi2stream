package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;

import com.github.aiderpmsi.pims.parser.linestypes.LineBuilder;
import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.parser.utils.Utils.LineHandler;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treemodel.Node;

public class SimpleParser {

	private final Node<?> tree;

	private final String type;
	
	private final LineConfDictionary dico;

	private final LineHandler lineWriter;
	
	public SimpleParser(
			final Node<?> tree,
			final LineConfDictionary dico,
			final String type,
			final LineHandler lineWriter) throws TreeBrowserException {
		this.tree = tree;
		this.type = type;
		this.dico = dico;
		this.lineWriter = lineWriter;
	}

	@Override
	public void parse(final Reader reader) throws IOException {
		// CREATES THE MEMORYBUFFERED READER (KEEPS IN MEMORY THE LAST READING LINE AND CONSUMES ONLY EXPLICITELY CHARACTERS)
		final MemoryBufferedReader mbr = new MemoryBufferedReader(reader);

		HashMap<String, Object> context = new HashMap<>();
		context.put("lb", new LineBuilder(dico));
		context.put("br", mbr);
		context.put("ch", ch);
		context.put("eh", ch);
		context.put("utils", new Utils(mbr, lineWriter, eh));
		context.put("start", type);
	
		JexlContext jc = new MapContext(context);

		boolean started = false;
		try {
			getContentHandler().startDocument();
			started = true;

			getContentHandler().startElement("", "root", "root", new Attributes2Impl());

			// CREATES AND EXECUTES THE TREE BROWSER
			TreeBrowser tb = new TreeBrowser(tree);
			tb.setJc(jc);

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
			if (started)
				getContentHandler().endDocument();
		}
	}

	public String getType() {
		return type;
	}

	public void setLineWriter(LineHandler lineWriter) {
		this.lineWriter = lineWriter;
	}

}


