package com.github.aiderpmsi.pims.grouper.tags;

import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treebrowser.actions.SimpleActionFactory;
import com.github.aiderpmsi.pims.treemodel.Node;

public class GroupFactory extends SimpleActionFactory {

	protected enum GroupField implements Field {
		erreur("", true),
		racine("", true),
		modalite("", true),
		gravite("", true);
		
		private final String defaultValue;
		private final boolean mandatory;
		
		private GroupField(final String defaultValue, final boolean mandatory) {
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

	public GroupFactory() {
		super(GroupField.values());
	}

	@Override
	public final IAction createSimpleAction(final JexlEngine je,
			final HashMap<String, String> arguments) throws IOException {

		
		return (final Node<?> node, final JexlContext jc) -> {
			jc.set("group", arguments);
			return node.firstChild;
		};

	}

}
