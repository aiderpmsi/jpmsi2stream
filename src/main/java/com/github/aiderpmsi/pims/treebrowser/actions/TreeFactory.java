package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class TreeFactory implements ActionFactory<TreeFactory.Tree> {

	@Override
	public Tree createAction(JexlEngine je, Argument[] arguments) throws IOException {
		// NO ARGUMENTS
		return new Tree();
	}

	public class Tree implements ActionFactory.Action {
		
		@SuppressWarnings("unchecked")
		@Override
		public Node<Action> execute(Node<Action> node,
				JexlContext jc) throws IOException {

			return (Node<ActionFactory.Action>) node.firstChild;
		}
	}
	
}
