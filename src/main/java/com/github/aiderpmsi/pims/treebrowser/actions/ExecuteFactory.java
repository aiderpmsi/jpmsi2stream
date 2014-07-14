package com.github.aiderpmsi.pims.treebrowser.actions;

import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;

import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treemodel.Node;

public class ExecuteFactory extends SimpleActionFactory {

	protected enum ExecuteField implements Field {
		expr(null, true);
		
		private final String defaultValue;
		private final boolean mandatory;
		
		private ExecuteField(final String defaultValue, final boolean mandatory) {
			this.defaultValue = defaultValue;
			this.mandatory = mandatory;
		}
		@Override public String getDefaultValue() {
			return defaultValue;
		}
		@Override public boolean isMandatory() {
			return mandatory;
		}
	}

	public ExecuteFactory() {
		super(ExecuteField.values());
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws TreeBrowserException {

		return new IAction() {
			
			private Script e = null;

			@Override
			public final Node<?> execute(final Node<?> node, final JexlContext jc) throws TreeBrowserException {
				if (e == null)
					e = je.createScript(arguments.get("expr"));
		        // RUNS THE EXPRESSION
				e.execute(jc);
				// RETURNS A NODE
				return node.firstChild == null ? node.nextSibling : node.firstChild;				
			}
		};

	}

}
