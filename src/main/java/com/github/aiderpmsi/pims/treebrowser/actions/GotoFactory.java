package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class GotoFactory extends SimpleActionFactory {

	private final static HashSet<String> neededArguments = new HashSet<>(1);
	
	private final static HashMap<String, String> defaultArgumentsValues = new HashMap<>(0);
	
	static {
		neededArguments.add("dest");
	}
	
	public GotoFactory() {
		super(neededArguments, defaultArgumentsValues);
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException {

		return new IAction() {
			
			private Node<?> dest = null;
			
			@Override
			public final Node<?> execute(final Node<?> node, final JexlContext jc) throws IOException {
				if (dest == null) {
					if ((dest = node.index.get(arguments.get("dest"))) == null) {
						throw new IOException("id " + arguments.get("dest") + " not found");
					} else {
						return dest;
					}
				} else {
					return dest;
				}
			}
		};

	}
	
}
