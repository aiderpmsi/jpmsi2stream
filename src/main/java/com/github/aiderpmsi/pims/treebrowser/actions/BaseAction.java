package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public abstract class BaseAction implements Action {

	@SuppressWarnings("unchecked")
	@Override
	public Node<Action> execute(Node<Action> node,
			HashMap<String, Node<Action>> labels, JexlContext jc,
			JexlEngine jexl) throws IOException {
		execute(jc, jexl);
		return (Node<Action>) (node.firstChild == null ?
				node.nextSibling :
					node.firstChild);
	}
	
	public abstract void execute(JexlContext jc,
			JexlEngine jexl) throws IOException;

}
