package com.github.aiderpmsi.pims.treebrowser.actions;

import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;

public abstract class SimpleActionFactory implements IActionFactory {

	private final HashMap<String, String> defaultArguments;
	
	private final Field[] fields;
	
	public SimpleActionFactory(final Field[] fields) {
		this.fields = fields;
		this.defaultArguments = new HashMap<>(fields.length);
		for (final Field fieldDefinition : fields) {
			this.defaultArguments.put(fieldDefinition.toString(), fieldDefinition.getDefaultValue());
		}
	}
	
	@Override
	public final IAction createAction(final JexlEngine je, final Collection<Argument> arguments)
			throws TreeBrowserException {
		// 1 - VALIDATES THE ARGUMENTS BY PUTTING THEM IN A NEW HASHMAP
		@SuppressWarnings("unchecked")
		final HashMap<String, String> filledArguments = (HashMap<String, String>) defaultArguments.clone();
		// FILLS THE FIELDDEFINTIONS WITH THE ARGUMENTS, REPLACING IF NEEDED
		for (Argument argument : arguments) {
			filledArguments.put(argument.getName(), argument.getValue());
		}
		// CHECK THAT EVERY NOT NULL FIELD IS NOT NULL
		for (Field field : fields) {
			if (field.isMandatory() && filledArguments.get(field.toString()) == null) {
				throw new TreeBrowserException("Arguments do not validate : field " + field.toString() + " is mandatory");
			}
		}
		
		// IF WE ARE THERE, EVERY MANDATORY FIELD IS FILLED, CREATES THE ACTION
		final IAction simpleAction = createSimpleAction(je, filledArguments);
		return (node, jc) -> simpleAction.execute(node, jc);
	}
	
	public abstract IAction createSimpleAction(
			final JexlEngine je,
			final HashMap<String, String> arguments) throws TreeBrowserException;

	protected interface Field {
		public String getDefaultValue();
		public boolean isMandatory();
	}

}
