package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.parser.linestypes.PmsiLineType.LineWriter;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public class ParserFactory {

	private Node<ActionFactory.Action> tree;
	
	private LineConfDictionary dico;
	
	private LineWriter lineWriter;

	@SuppressWarnings("unchecked")
	public ParserFactory(LineWriter lineWriter) throws TreeBrowserException {
		ParserConfigBuilder pcb = new ParserConfigBuilder();
		tree = (Node<Action>) pcb.build();
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
