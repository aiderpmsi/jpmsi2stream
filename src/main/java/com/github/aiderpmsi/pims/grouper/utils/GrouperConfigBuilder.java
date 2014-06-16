package com.github.aiderpmsi.pims.grouper.utils;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jexl2.JexlEngine;
import com.github.aiderpmsi.pims.grouper.tags.GroupFactory;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserBuilder;

public class GrouperConfigBuilder extends TreeBrowserBuilder {

	public GrouperConfigBuilder() {
		super(treeLocation);
	}

	private static final String treeLocation = "com/github/aiderpmsi/pims/grouper/grouper.xml";

	@Override
	protected List<ActionDefinition> getCustomActions() {
		LinkedList<ActionDefinition> customActions = new LinkedList<>();
		
		ActionDefinition groupAction = new ActionDefinition();
		groupAction.nameSpace = "http://custom.actions/pims";
		groupAction.command = "group";
		groupAction.actionFactory = new GroupFactory();
		
		customActions.add(groupAction);

		return customActions;
	}

	@Override
	protected JexlEngine getJexlEngine() {
		return new JexlEngine();
	}
		
}
