package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public class ParserFactory {

	private Node<ActionFactory.Action> tree;
	
	private LineConfDictionary dico;

	@SuppressWarnings("unchecked")
	public ParserFactory() throws TreeBrowserException {
		ParserConfigBuilder pcb = new ParserConfigBuilder();
		tree = (Node<Action>) pcb.build();
		dico = new LineConfDictionary();
	}
	
	public Parser newParser(String type) throws IOException {
		try {
			return new Parser(tree, dico, type);
		} catch (TreeBrowserException e) {
			throw new IOException(e);
		}
	}

}
