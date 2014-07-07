package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.jexl2.JexlEngine;

public class TreeFactory implements IActionFactory {

	@Override
	public final IAction createAction(final JexlEngine je, final Collection<Argument> arguments) throws IOException {
		return (node, jc) -> node.firstChild;
	}
	
}
