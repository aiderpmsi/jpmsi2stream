package com.github.aiderpmsi.pims.grouper.tags;

import java.io.IOException;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

import com.github.aiderpmsi.pims.treebrowser.Action;
import com.github.aiderpmsi.pims.treebrowser.Argument;

public class Group extends Action {
	
	private String erreur,	racine,
			modalite, gravite;

	public String getErreur() {
		return erreur;
	}

	public void setErreur(String erreur) {
		this.erreur = erreur;
	}

	public String getRacine() {
		return racine;
	}

	public void setRacine(String racine) {
		this.racine = racine;
	}

	public String getModalite() {
		return modalite;
	}

	public void setModalite(String modalite) {
		this.modalite = modalite;
	}

	public String getGravite() {
		return gravite;
	}

	public void setGravite(String gravite) {
		this.gravite = gravite;
	}

	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl, Argument[] args) throws IOException {
		// GETS ARGUMENTS
		erreur = "";
		racine = "";
		modalite = "";
		gravite = "";
		for (Argument arg : args) {
			switch (arg.key) {
			case "erreur":
				erreur = arg.value; break;
			case "racine":
				racine = arg.value; break;
			case "modalite":
				modalite = arg.value; break;
			case "gravite":
				gravite = arg.value; break;
			default:
				throw new IOException("Argument " + arg.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// SETS THE GROUP
		jc.set("group", this);
		return "child(1)";
	}

}
