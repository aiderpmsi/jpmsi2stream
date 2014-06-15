package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class Goto implements Action {

	String id;
	
	Goto(String id) {
		this.id = id;
	}
	
	@Override
	public Node<Action> execute(Node<Action> node,
			HashMap<String, Node<Action>> labels, JexlContext jc,
			JexlEngine jexl) throws IOException {
		Node<Action> toGoto;
		if ((toGoto = labels.get(id)) == null) {
			throw new IOException("id " + id + " not found");
		} else {
			return toGoto;
		}
	}

}
