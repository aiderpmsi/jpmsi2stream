package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import javax.script.ScriptContext;

import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public abstract class BaseAction implements Action {

	@SuppressWarnings("unchecked")
	@Override
	public Node<Action> execute(Node<Action> node,
			ScriptContext sc) throws IOException {
		execute(sc);
		return (Node<Action>) (node.firstChild == null ?
				node.nextSibling :
					node.firstChild);
	}
	
	public abstract void execute(ScriptContext sc) throws IOException;

}
