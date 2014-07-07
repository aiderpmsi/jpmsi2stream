package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class AssignFactory extends SimpleActionFactory {
	
	protected enum AssignField implements Field {
		expr(null, true),
		var(null, true);
		
		private final String defaultValue;
		private final boolean mandatory;
		private AssignField(final String defaultValue, final boolean mandatory) {
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
	
	public AssignFactory() {
		super(AssignField.values());
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException {

		return new IAction() {
			
			private Expression e = null;
			
			@Override
			public final Node<?> execute(final Node<?> node, final JexlContext jc) throws IOException {
				if (e == null)
					e = je.createExpression(arguments.get("expr"));
		        // RUNS THE EXPRESSION
				jc.set(arguments.get("var"), e.evaluate(jc));
				// RETURNS A NODE
				return node.firstChild == null ? node.nextSibling : node.firstChild;				
			}
		};

	}

}
