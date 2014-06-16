package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import javax.script.Compilable;
import javax.script.ScriptContext;

import com.github.aiderpmsi.pims.treemodel.Node;

public class TreeFactory implements ActionFactory<TreeFactory.Tree> {

	@Override
	public Tree createAction(Compilable se, Argument[] arguments) throws IOException {
		// NO ARGUMENTS
		return new Tree();
	}

	public class Tree implements ActionFactory.Action {
		
		@SuppressWarnings("unchecked")
		@Override
		public Node<Action> execute(Node<Action> node,
				ScriptContext jc) throws IOException {

			return (Node<ActionFactory.Action>) node.firstChild;
		}
	}
	
}
