package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;

import javax.script.ScriptContext;

import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public class TreeBrowser {

	/** jexl2 context map */
	private ScriptContext sc = null;

	/** Tree source */
	private Node<Action> tree = null;

	public TreeBrowser(Node<Action> tree) {
		this.tree = tree;
	}
	
	public void go() throws IOException {
		while (tree != null) {
			tree = tree.getContent().execute(tree, sc);
		}
	}    
	
	public ScriptContext getContext() {
		return sc;
	}

	public void setContext(ScriptContext sc) {
		this.sc = sc;
	}

}
