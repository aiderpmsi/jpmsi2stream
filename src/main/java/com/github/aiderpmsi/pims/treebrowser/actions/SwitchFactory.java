package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.aiderpmsi.pims.treemodel.Node;

public class SwitchFactory implements ActionFactory<SwitchFactory.Switch> {

	@Override
	public Switch createAction(Compilable se, Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String cond = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "cond":
				cond = argument.value; break;
			case "id": break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (cond == null) {
			throw new IOException("cond parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Switch(se, cond);
	}

	public class Switch implements ActionFactory.Action {
		
		private CompiledScript e; 
		
		public Switch(Compilable se, String cond) {
			try {
				e = se.compile(cond);
			} catch (ScriptException e) {
				throw new RuntimeException(e);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Node<Action> execute(Node<Action> node,
				ScriptContext sc) throws IOException {

			try {
				Object result = e.eval(sc);
	        
		        if (result != null && result instanceof Boolean) {
		        	Boolean resultB = (Boolean) result;
		        	if (resultB)
		        		return (Node<ActionFactory.Action>) node.firstChild;
		        	else
		        		return (Node<ActionFactory.Action>) node.nextSibling;
		        } else {
		        	throw new IOException(result == null ? "Null" : result.toString() + " is not of boolean type");
		        }
			} catch (ScriptException e) {
				throw new IOException(e);
			}
		}
	}
	
}
