package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;

public class ExecuteFactory implements ActionFactory<ExecuteFactory.Execute> {

	@Override
	public Execute createAction(Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String expr = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "expr":
				expr = argument.value; break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (expr == null) {
			throw new IOException("expr parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Execute(expr);
	}

	public class Execute extends BaseAction {
		
		private String expr;
		
		private Script e = null;
		
		public Execute(String expr) {
			this.expr = expr;
		}
		
		@Override
		public void execute(JexlContext jc, JexlEngine jexl) throws IOException {
	        // CREATES THE EXPRESSION IF NEEDED
			if (e == null) {
				e = jexl.createScript(expr);
			}
	        // RUNS THE EXPRESSION
			e.execute(jc);
		}
	}

}
