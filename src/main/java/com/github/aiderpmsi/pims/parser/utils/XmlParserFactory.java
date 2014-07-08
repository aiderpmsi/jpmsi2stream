package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.parser.utils.XmlParser.XmlErrorHandler;
import com.github.aiderpmsi.pims.parser.utils.XmlParser.XmlLineHandler;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserFactory;
import com.github.aiderpmsi.pims.treemodel.Node;

public class XmlParserFactory {

	private static final String treeLocation = "com/github/aiderpmsi/pims/parser/parser.xml";

	private final Node<?> tree;
	
	private final LineConfDictionary dico;
	
	public XmlParserFactory() throws TreeBrowserException {
		final TreeBrowserFactory tbf = new TreeBrowserFactory(
				treeLocation, () -> new JexlEngine());
		tree = tbf.newTree();
		dico = new LineConfDictionary();
	}
	
	public XmlParser newParser(final String type, final XmlLineHandler xmlLineHandler, final XmlErrorHandler xmlErrorHandler) throws IOException {
		try {
			return new XmlParser(tree, dico, type, xmlLineHandler, xmlErrorHandler);
		} catch (TreeBrowserException e) {
			throw new IOException(e);
		}
	}

}
