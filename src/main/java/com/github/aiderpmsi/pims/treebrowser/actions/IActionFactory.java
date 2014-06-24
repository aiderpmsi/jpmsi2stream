package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;


@FunctionalInterface
public interface IActionFactory {

	public IAction createAction(final JexlEngine je, final HashMap<String, String> arguments) throws IOException;
	
	@FunctionalInterface
	public interface IAction {

		public Node<?> execute(final Node<?> node,
				final JexlContext jc) throws IOException;

	}

}
