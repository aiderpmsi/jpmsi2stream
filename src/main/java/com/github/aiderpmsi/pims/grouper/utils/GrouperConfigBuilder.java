package com.github.aiderpmsi.pims.grouper.utils;

import java.util.LinkedList;
import java.util.List;

import javax.script.Compilable;
import javax.script.ScriptEngineManager;

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
	protected Compilable getScriptEngine() {
		ScriptEngineManager manager = new ScriptEngineManager();
		Compilable js = (Compilable) manager.getEngineByName("js");
		if (js == null)
			throw new RuntimeException("Javascript engine not found");
		return js;
	}
		
}
