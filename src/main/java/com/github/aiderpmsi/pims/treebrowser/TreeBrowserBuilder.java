package com.github.aiderpmsi.pims.treebrowser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jexl2.JexlEngine;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treebrowser.actions.AssignFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.ExecuteFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.GotoFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.SwitchFactory;
import com.github.aiderpmsi.pims.treemodel.Node;
import com.github.aiderpmsi.pims.treemodel.TreeContentHandler;

public abstract class TreeBrowserBuilder {

	private String resource;
	
	public TreeBrowserBuilder(String resource) {
		this.resource = resource;
	}
	
	public Node<?> build() throws TreeBrowserException {
		// CREATES THE JEXL ENGINE
		JexlEngine je = getJexlEngine();
		
		// CREATES THE LIST OF ACTION DEFINITIONS
		List<ActionDefinition> actionDefinitions = new LinkedList<>();
		// BUILD IN
		for (Object[] dfltaction : dfltactions) {
			ActionDefinition aDef = new ActionDefinition();
			aDef.nameSpace = (String) dfltaction[0];
			aDef.command = (String) dfltaction[1];
			aDef.actionFactory = (ActionFactory<?>) dfltaction[2];
			actionDefinitions.add(aDef);
		}
		// CUSTOM
		actionDefinitions.addAll(getCustomActions());
		
		// CREATES THE ACTION TREE
		try (InputStream is = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(resource))) {
			Node<Action> tree = createTree(is, actionDefinitions, je);
			return tree;
		} catch (IOException | SAXException e) {
			throw new TreeBrowserException(e);
		}
	}

	private Node<Action> createTree(InputStream is, List<ActionDefinition> actionDefinitions, JexlEngine je) throws SAXException, IOException {
		// CREATES THE CONTENTHANDLER
		TreeContentHandler tc = new TreeContentHandler();
		for (ActionDefinition actionDefinition : actionDefinitions) {
			tc.addAction(actionDefinition.nameSpace, actionDefinition.command, actionDefinition.actionFactory);
		}
		tc.setEngine(je);
		
		// CREATES THE XML READER
		XMLReader saxReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        saxReader.setContentHandler(tc);

        // PARSES THE TREE DEFINITION
        saxReader.parse(new InputSource(is));

        // RETURNS THE TREE OF ACTIONS
        return tc.getTree();
	}
	
	public class ActionDefinition {
		public String nameSpace, command;
		public ActionFactory<?> actionFactory;
	}
	
	protected abstract List<ActionDefinition> getCustomActions();
	
	protected abstract JexlEngine getJexlEngine();
	
	/** Default actions */
	private static final Object[][] dfltactions = {
			{"http://default.actions/default", "execute", new ExecuteFactory()},
			{"http://default.actions/default", "assign", new AssignFactory()},
			{"http://default.actions/default", "switch", new SwitchFactory()},
			{"http://default.actions/default", "goto", new GotoFactory()},
	};
}
