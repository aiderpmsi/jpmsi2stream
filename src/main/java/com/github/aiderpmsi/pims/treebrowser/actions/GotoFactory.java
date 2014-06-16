package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class GotoFactory implements ActionFactory<GotoFactory.Goto> {

	@Override
	public Goto createAction(JexlEngine je, Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String toid = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "toid":
				toid = argument.value; break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (toid == null) {
			throw new IOException("id parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Goto(toid);
	}

	public class Goto implements ActionFactory.Action {
		
		private String toid;
		
		public Goto(String toid) {
			this.toid = toid;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Node<Action> execute(Node<Action> node,
				JexlContext jc) throws IOException {
			Node<Action> toGoto;
			if ((toGoto = (Node<ActionFactory.Action>) node.index.get(toid)) == null) {
				throw new IOException("id " + toid + " not found");
			} else {
				return toGoto;
			}
		}
	}
	
}
