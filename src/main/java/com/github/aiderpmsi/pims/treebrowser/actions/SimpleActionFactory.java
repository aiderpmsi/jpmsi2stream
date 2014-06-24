package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public abstract class SimpleActionFactory implements IActionFactory {

	private final HashSet<String> neededArguments;
	
	private final HashMap<String, String> defaultArgumentsValues;
	
	public SimpleActionFactory(
			final HashSet<String> neededArguments,
			final HashMap<String, String> defaultArgumentsValues) {
		this.neededArguments = neededArguments;
		this.defaultArgumentsValues = defaultArgumentsValues;
		
	}
	
	@Override
	public final IAction createAction(final JexlEngine je, final HashMap<String, String> arguments)
			throws IOException {
		if (validate(arguments)) {
			final IAction simpleAction = createSimpleAction(je, arguments);
			return (final Node<?> node, final JexlContext jc) -> {
				return simpleAction.execute(node, jc);
			};
		} else {
			throw new IOException("Arguments do not validate");
		}
	}
	
	public abstract IAction createSimpleAction(
			final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException;

	private final boolean validate(final HashMap<String, String> arguments) {
		for (String neededArgument : neededArguments) {
			if (arguments.get(neededArgument) == null) {
				final String argumentValue;
				if ((argumentValue = defaultArgumentsValues.get(neededArgument)) != null) {
					arguments.put(neededArgument, argumentValue);
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
}
