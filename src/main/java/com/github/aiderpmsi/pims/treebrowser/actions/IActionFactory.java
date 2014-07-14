package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treemodel.Node;

@FunctionalInterface
public interface IActionFactory {

	/**
	 * 
	 * @param je
	 * @param arguments {@link Collection} of {@link Argument}, ordered by 
	 * @return
	 * @throws IOException
	 */
	public IAction createAction(final JexlEngine je, final Collection<Argument> arguments) throws TreeBrowserException;
	
	@FunctionalInterface
	public interface IAction {

		public Node<?> execute(final Node<?> node, final JexlContext jc) throws TreeBrowserException;

	}

	public class Argument {
		
		private String name = null;
		
		private String value = null;

		public Argument(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}


}
