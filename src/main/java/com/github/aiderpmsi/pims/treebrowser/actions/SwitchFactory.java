package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class SwitchFactory extends SimpleActionFactory {

	protected enum SwitchField implements Field {
		cond(null, true);
		
		private final String defaultValue;
		private final boolean mandatory;
		
		private SwitchField(final String defaultValue, final boolean mandatory) {
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
	
	public SwitchFactory() {
		super(SwitchField.values());
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException {

		return new IAction() {
			
			private Expression e = null; 

			@Override
			public final Node<?> execute(final Node<?> node, final JexlContext jc) throws IOException {
				if (e == null) {
					e = je.createExpression(arguments.get("cond"));
				}
				
				Object result = e.evaluate(jc);
		        
		        if (result != null && result instanceof Boolean) {
		        	Boolean resultB = (Boolean) result;
		        	if (resultB)
		        		return (Node<?>) node.firstChild;
		        	else
		        		return (Node<?>) node.nextSibling;
		        } else {
		        	throw new IOException(result == null ? "Null" : result.toString() + " is not of boolean type");
		        }
			}
		};

	}
	
}
