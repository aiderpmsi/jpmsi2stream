package com.github.aiderpmsi.pims.treebrowser.actions;


public interface ActionFactory<T extends Action> {

	public T createAction(Argument[] arguments);
	
}
