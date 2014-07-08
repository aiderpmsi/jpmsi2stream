package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.parser.utils.Utils.ErrorHandler;
import com.github.aiderpmsi.pims.parser.utils.Utils.LineHandler;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserFactory;
import com.github.aiderpmsi.pims.treemodel.Node;

public class SimpleParserFactory {

	private static final String treeLocation = "com/github/aiderpmsi/pims/parser/parser.xml";

	private final Node<?> tree;
	
	private final LineConfDictionary dico;
	
	public SimpleParserFactory() throws TreeBrowserException {
		final TreeBrowserFactory tbf = new TreeBrowserFactory(
				treeLocation, () -> new JexlEngine());
		tree = tbf.newTree();
		dico = new LineConfDictionary();
	}
	
	public SimpleParser newParser(final String type, final Collection<LineHandler> lineHandlers, final ErrorHandler errorHandler) throws IOException {
		try {
			return new SimpleParser(tree, dico, type, lineHandlers, errorHandler);
		} catch (TreeBrowserException e) {
			throw new IOException(e);
		}
	}

}
