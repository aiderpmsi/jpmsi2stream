package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class GotoFactory extends SimpleActionFactory {

	protected enum GotoField implements Field {
		dest(null, true);
		
		private final String defaultValue;
		private final boolean mandatory;
		
		private GotoField(final String defaultValue, final boolean mandatory) {
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

	public GotoFactory() {
		super(GotoField.values());
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException {

		return new IAction() {
			
			private Node<?> dest = null;
			
			@Override
			public final Node<?> execute(final Node<?> node, final JexlContext jc) throws IOException {
				if (dest == null) {
					if ((dest = node.index.get(arguments.get("dest"))) == null) {
						throw new IOException("id " + arguments.get("dest") + " not found");
					} else {
						return dest;
					}
				} else {
					return dest;
				}
			}
		};

	}
	
}
