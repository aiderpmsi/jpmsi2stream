package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class TreeFactory implements IActionFactory {

	@Override
	public final IAction createAction(final JexlEngine je, final HashMap<String, String> arguments) throws IOException {
		return (final Node<?> node, final JexlContext jc) -> node.firstChild;
	}
	
}
