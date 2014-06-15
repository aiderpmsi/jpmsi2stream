package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class GotoFactory implements ActionFactory<GotoFactory.Goto> {

	@Override
	public Goto createAction(Argument[] arguments) throws IOException {
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
		
		@Override
		public Node<Action> execute(Node<Action> node,
				HashMap<String, Node<Action>> labels, JexlContext jc,
				JexlEngine jexl) throws IOException {
			Node<Action> toGoto;
			if ((toGoto = labels.get(toid)) == null) {
				throw new IOException("id " + toid + " not found");
			} else {
				return toGoto;
			}
		}
	}
	
}
