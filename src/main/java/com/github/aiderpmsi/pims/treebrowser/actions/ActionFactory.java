package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import javax.script.Compilable;
import javax.script.ScriptContext;
import com.github.aiderpmsi.pims.treemodel.Node;


public interface ActionFactory<T extends ActionFactory.Action> {

	public T createAction(Compilable se, Argument[] arguments) throws IOException;
	
	public interface Action {

		public Node<Action> execute(Node<Action> node,
				ScriptContext sc) throws IOException;

	}
}
