package com.github.aiderpmsi.pims.parser.utils;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treebrowser.TreeBrowserBuilder;

public class ParserConfigBuilder extends TreeBrowserBuilder {

	public ParserConfigBuilder() {
		super(treeLocation);
	}

	private static final String treeLocation = "com/github/aiderpmsi/pims/parser/parser.xml";

	@Override
	protected List<ActionDefinition> getCustomActions() {
		LinkedList<ActionDefinition> customActions = new LinkedList<>();
		// NO NEW ACTION
		return customActions;
	}

	@Override
	protected JexlEngine getJexlEngine() {
		return new JexlEngine();
	}
		
}
