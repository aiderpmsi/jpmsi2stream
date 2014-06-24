package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.parser.linestypes.PmsiLineType.LineWriter;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory.IAction;
import com.github.aiderpmsi.pims.treemodel.Node;

public class ParserFactory {

	private Node<IActionFactory.IAction> tree;
	
	private LineConfDictionary dico;
	
	private LineWriter lineWriter;

	@SuppressWarnings("unchecked")
	public ParserFactory(LineWriter lineWriter) throws TreeBrowserException {
		ParserConfigBuilder pcb = new ParserConfigBuilder();
		tree = (Node<IAction>) pcb.build();
		dico = new LineConfDictionary();
		this.lineWriter = lineWriter;
	}
	
	public Parser newParser(String type) throws IOException {
		try {
			return new Parser(tree, dico, lineWriter, type);
		} catch (TreeBrowserException e) {
			throw new IOException(e);
		}
	}

}
