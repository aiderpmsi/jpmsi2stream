package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import org.apache.commons.jexl2.JexlContext;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory.IAction;
import com.github.aiderpmsi.pims.treemodel.Node;

public class TreeBrowser {

	/** jexl2 context map */
	private JexlContext jc = null;

	/** Tree source */
	private Node<?> tree = null;

	public TreeBrowser(Node<?> tree) {
		this.tree = tree;
	}
	
	public void go() throws IOException {
		while (tree != null) {
			if (tree.getContent() instanceof IAction) {
				tree = ((IAction) tree.getContent()).execute(tree, jc);
			} else {
				throw new IOException("Node is not an action node");
			}
		}
	}    
	
	public JexlContext getJc() {
		return jc;
	}

	public void setJc(JexlContext jc) {
		this.jc = jc;
	}

}
