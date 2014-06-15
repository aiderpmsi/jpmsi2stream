package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

public class GotoFactoy implements ActionFactory<Goto> {

	@Override
	public Goto createAction(Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String id = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "id":
				id = argument.value; break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (id == null) {
			throw new IOException("id parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Goto(id);
	}

}
