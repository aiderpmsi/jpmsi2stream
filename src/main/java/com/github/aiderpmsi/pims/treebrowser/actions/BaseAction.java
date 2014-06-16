package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import org.apache.commons.jexl2.JexlContext;

import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public abstract class BaseAction implements Action {

	@SuppressWarnings("unchecked")
	@Override
	public Node<Action> execute(Node<Action> node,
			JexlContext jc) throws IOException {
		execute(jc);
		return (Node<Action>) (node.firstChild == null ?
				node.nextSibling :
					node.firstChild);
	}
	
	public abstract void execute(JexlContext jc) throws IOException;

}
