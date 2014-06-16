package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

public class AssignFactory implements ActionFactory<AssignFactory.Assign> {

	@Override
	public Assign createAction(JexlEngine je, Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String expr = null, var = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "expr":
				expr = argument.value; break;
			case "var":
				var = argument.value; break;
			case "id": break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (expr == null) {
			throw new IOException("expr parameter has to be set in " + getClass().getSimpleName());
		} else if (var == null) {
			throw new IOException("var parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Assign(je, var, expr);
	}

	public class Assign extends BaseAction {
		
		private Expression e;
		
		private String var;
		
		public Assign(JexlEngine je, String var, String expr) {
			e = je.createExpression(expr);
			this.var = var;
		}
		
		@Override
		public void execute(JexlContext jc) throws IOException {
	        // RUNS THE EXPRESSION
			jc.set(var, e.evaluate(jc));
		}
	}

}
