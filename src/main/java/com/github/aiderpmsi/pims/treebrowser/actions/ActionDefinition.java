package com.github.aiderpmsi.pims.treebrowser.actions;


public class ActionDefinition {
	public final String nameSpace, command;
	public final IActionFactory actionFactory;
	
	public ActionDefinition(final String nameSpace, final String command, final IActionFactory actionFactory) {
		this.nameSpace = nameSpace;
		this.command = command;
		this.actionFactory = actionFactory;
	}
}

