package com.github.aiderpmsi.pims.grouper.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treebrowser.actions.SimpleActionFactory;
import com.github.aiderpmsi.pims.treemodel.Node;

public class GroupFactory extends SimpleActionFactory {

	private final static HashSet<String> neededArguments = new HashSet<>(4);
	
	private final static HashMap<String, String> defaultArgumentsValues = new HashMap<>(4);
	
	static {
		neededArguments.add("erreur");defaultArgumentsValues.put("erreur", "");
		neededArguments.add("racine");defaultArgumentsValues.put("racine", "");
		neededArguments.add("modalite");defaultArgumentsValues.put("modalite", "");
		neededArguments.add("gravite");defaultArgumentsValues.put("gravite", "");
	}
	
	public GroupFactory() {
		super(neededArguments, defaultArgumentsValues);
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException {

		
		return (final Node<?> node, final JexlContext jc) -> {
			jc.set("group", arguments);
			return node.firstChild;
		};

	}

}
