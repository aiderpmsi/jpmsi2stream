package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

public class AssignFactory implements ActionFactory<AssignFactory.Assign> {

	@Override
	public Assign createAction(Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String expr = null, var = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "expr":
				expr = argument.value; break;
			case "var":
				var = argument.value; break;
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
		
		return new Assign(var, expr);
	}

	public class Assign extends BaseAction {
		
		private String var, expr;
		
		private Expression e = null;
		
		public Assign(String var, String expr) {
			this.var = var;
			this.expr = expr;
		}
		
		@Override
		public void execute(JexlContext jc, JexlEngine jexl) throws IOException {
	        // CREATES THE EXPRESSION IF NEEDED
			if (e == null) {
				e = jexl.createExpression(expr);
			}
	        // RUNS THE EXPRESSION
			jc.set(var, e.evaluate(jc));
		}
	}

}
