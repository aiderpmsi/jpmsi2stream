package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;

import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.tags.GroupFactory;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionDefinition;
import com.github.aiderpmsi.pims.treemodel.Node;

public class GrouperFactory {

	private static final String treeLocation = "com/github/aiderpmsi/pims/grouper/grouper.xml";

	private final Node<?> tree;
	
	private final Dictionaries dicos = new Dictionaries();
		
	public GrouperFactory() throws TreeBrowserException {
		final TreeBrowserFactory tbf = new TreeBrowserFactory(
				treeLocation, () -> new JexlEngine());
		tbf.addActionDefinition(new ActionDefinition("http://custom.actions/pims", "group", new GroupFactory()));
		tree = tbf.newTree();
	}
	
	public Grouper newGrouper() throws IOException {
		try {
			return new Grouper(tree, dicos);
		} catch (TreeBrowserException e) {
			throw new IOException(e);
		}
	}

}
