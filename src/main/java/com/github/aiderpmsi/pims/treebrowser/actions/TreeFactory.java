package com.github.aiderpmsi.pims.treebrowser.actions;

import java.util.Collection;

import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;

public class TreeFactory implements IActionFactory {

	@Override
	public final IAction createAction(final JexlEngine je, final Collection<Argument> arguments) throws TreeBrowserException {
		return (node, jc) -> node.firstChild;
	}
	
}
