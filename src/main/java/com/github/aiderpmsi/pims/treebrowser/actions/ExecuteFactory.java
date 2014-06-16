package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

public class ExecuteFactory implements ActionFactory<ExecuteFactory.Execute> {

	@Override
	public Execute createAction(Compilable se, Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String expr = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "expr":
				expr = argument.value; break;
			case "id": break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (expr == null) {
			throw new IOException("expr parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Execute(se, expr);
	}

	public class Execute extends BaseAction {
		
		private CompiledScript e;
		
		public Execute(Compilable se, String expr) {
			try {
				e = se.compile(expr);
			} catch (ScriptException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void execute(ScriptContext sc) throws IOException {
	        // RUNS THE EXPRESSION
			try {
				e.eval(sc);
			} catch (ScriptException e) {
				throw new IOException(e);
			}
		}
	}

}
