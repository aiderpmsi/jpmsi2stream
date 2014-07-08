package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;

import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory.IAction;
import com.github.aiderpmsi.pims.treemodel.Node;

public class TreeBrowser {

	/** jexl2 context map */
	private final JexlContext jc;

	/** Tree source */
	private final Node<?> root;
	
	/** boolean value indicationg if the treewalker must be stopped */
	private boolean interrupted = false;

	public TreeBrowser(final Node<?> root, final HashMap<String, Object> vars) {
		this.root = root;
		this.jc = new MapContext(vars);
	}
	
	public void go() throws Exception {
		Node<?> node = root;
		while (node != null) {
			if (node.getContent() instanceof IAction) {
				node = ((IAction) node.getContent()).execute(node, jc);
				// TEST IF THE OPERATION HAS BEEN INTERRUPTED
				synchronized(this) {
					if (interrupted) {
						interrupted = false;
						break;
					}
				}
			} else {
				throw new IOException("Node is not an action node");
			}
		}
	}    
	
	public Object getContextObject(final String varName) {
		return jc.get(varName);
	}

	public void setContextObject(final String varName, final Object content) {
		jc.set(varName, content);
	}

}
