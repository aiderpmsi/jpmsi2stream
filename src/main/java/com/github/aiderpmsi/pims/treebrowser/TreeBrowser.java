package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import org.apache.commons.jexl2.JexlContext;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public class TreeBrowser {

	/** jexl2 context map */
	private JexlContext jc = null;

	/** Tree source */
	private Node<Action> tree = null;

	public TreeBrowser(Node<Action> tree) {
		this.tree = tree;
	}
	
	public void go() throws IOException {
		while (tree != null) {
			tree = tree.getContent().execute(tree, jc);
		}
	}    
	
	public JexlContext getJc() {
		return jc;
	}

	public void setJc(JexlContext jc) {
		this.jc = jc;
	}

}
