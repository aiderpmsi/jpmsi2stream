package com.github.aiderpmsi.pims.grouper.tags;

import java.io.IOException;

import javax.script.Compilable;
import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.Argument;
import com.github.aiderpmsi.pims.treemodel.Node;

public class GroupFactory implements ActionFactory<GroupFactory.Group> {

	@Override
	public Group createAction(Compilable se, Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String erreur = "", racine = "", modalite = "", gravite = "";
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "erreur":
				erreur = argument.value; break;
			case "racine":
				racine = argument.value; break;
			case "modalite":
				modalite = argument.value; break;
			case "gravite":
				gravite = argument.value; break;
			case "id": break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		return new Group(erreur, racine, modalite, gravite);
	}

	public class Group implements ActionFactory.Action {
		
		public String erreur, racine, modalite, gravite;

		public Group(String erreur, String racine, String modalite, String gravite) {
			this.erreur = erreur;
			this.racine = racine;
			this.modalite = modalite;
			this.gravite = gravite;
		}

		@Override
		public Node<Action> execute(Node<Action> node, ScriptContext jc)
				throws IOException {
			jc.setAttribute("group", this, SimpleScriptContext.ENGINE_SCOPE);
			return null;
		}
		
	}

}
