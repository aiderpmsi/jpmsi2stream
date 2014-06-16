package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;


public interface ActionFactory<T extends ActionFactory.Action> {

	public T createAction(JexlEngine je, Argument[] arguments) throws IOException;
	
	public interface Action {

		public Node<Action> execute(Node<Action> node,
				JexlContext jc) throws IOException;

	}
}
