package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;


public interface ActionFactory<T extends Action> {

	public T createAction(Argument[] arguments) throws IOException;
	
}
