package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;

import com.github.aiderpmsi.pims.treemodel.Node;

public class ExecuteFactory extends SimpleActionFactory {

	private final static HashSet<String> neededArguments = new HashSet<>(1);
	
	private final static HashMap<String, String> defaultArgumentsValues = new HashMap<>(0);
	
	static {
		neededArguments.add("expr");
	}
	
	public ExecuteFactory() {
		super(neededArguments, defaultArgumentsValues);
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException {

		return new IAction() {
			
			private Script e = null;

			@Override
			public final Node<?> execute(final Node<?> node, final JexlContext jc) throws IOException {
				if (e == null)
					e = je.createScript(arguments.get("expr"));
		        // RUNS THE EXPRESSION
				e.execute(jc);
				// RETURNS A NODE
				return node.firstChild == null ? node.nextSibling : node.firstChild;				
			}
		};

	}

}
