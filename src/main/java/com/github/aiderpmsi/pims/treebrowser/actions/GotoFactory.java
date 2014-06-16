package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import javax.script.Compilable;
import javax.script.ScriptContext;

import com.github.aiderpmsi.pims.treemodel.Node;

public class GotoFactory implements ActionFactory<GotoFactory.Goto> {

	@Override
	public Goto createAction(Compilable se, Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String dest = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "dest":
				dest = argument.value; break;
			case "id": break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (dest == null) {
			throw new IOException("id parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Goto(dest);
	}

	public class Goto implements ActionFactory.Action {
		
		private String dest;
		
		public Goto(String dest) {
			this.dest = dest;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Node<Action> execute(Node<Action> node,
				ScriptContext sc) throws IOException {
			Node<Action> toGoto;
			if ((toGoto = (Node<ActionFactory.Action>) node.index.get(dest)) == null) {
				throw new IOException("id " + dest + " not found");
			} else {
				return toGoto;
			}
		}
	}
	
}
