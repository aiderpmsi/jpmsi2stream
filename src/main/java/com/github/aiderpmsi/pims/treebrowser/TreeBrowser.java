package com.github.aiderpmsi.pims.treebrowser;

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
	
	public void go() throws TreeBrowserException, InterruptedException {
		Node<?> node = root;
		try {
			while (node != null) {
				if (node.getContent() instanceof IAction) {
					try {
						node = ((IAction) node.getContent()).execute(node, jc);
					} catch (Throwable e) {
						if (e instanceof TreeBrowserException || e instanceof InterruptedException) {
							// DO NOTHING
						} else {
							throw new TreeBrowserException(e);
						}
					}
					// TEST IF THE OPERATION HAS BEEN INTERRUPTED
					synchronized(this) {
						if (Thread.interrupted())
							interrupted = true;
						if (interrupted) {
							interrupted = false;
							throw new InterruptedException();
						}
					}
				} else {
					throw new TreeBrowserException("Node is not an action node");
				}
			}
		} catch (InterruptedException | TreeBrowserException e) {
			throw e;
		} catch (Throwable e) {
			throw new TreeBrowserException(e);
		}
	}    
	
	public Object getContextObject(final String varName) {
		return jc.get(varName);
	}

	public void setContextObject(final String varName, final Object content) {
		jc.set(varName, content);
	}

}
