package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory.IAction;
import com.github.aiderpmsi.pims.treemodel.Node;

public class ParserFactory {

	private Node<IActionFactory.IAction> tree;
	
	private LineConfDictionary dico;
	
	@SuppressWarnings("unchecked")
	public ParserFactory() throws TreeBrowserException {
		ParserConfigBuilder pcb = new ParserConfigBuilder();
		tree = (Node<IAction>) pcb.build();
		dico = new LineConfDictionary();
	}
	
	public SimpleParser newParser(String type) throws IOException {
		try {
			return new SimpleParser(tree, dico, type);
		} catch (TreeBrowserException e) {
			throw new IOException(e);
		}
	}

}
