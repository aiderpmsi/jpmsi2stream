package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

public class AssignFactory implements ActionFactory<AssignFactory.Assign> {

	@Override
	public Assign createAction(Compilable se, Argument[] arguments) throws IOException {
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
		
		return new Assign(se, var, expr);
	}

	public class Assign extends BaseAction {
		
		private CompiledScript e;
		
		private String var;
		
		public Assign(Compilable se, String var, String expr) {

			try {
				e = se.compile(expr);
				this.var = var;
			} catch (ScriptException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void execute(ScriptContext sc) throws IOException {
	        // RUNS THE EXPRESSION
			try {
				Object result = e.eval(sc);
				sc.setAttribute(var, result, ScriptContext.ENGINE_SCOPE);
			} catch (ScriptException e) {
				throw new IOException(e);
			}
		}
	}

}
