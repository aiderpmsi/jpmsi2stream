package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.jexl2.JexlEngine;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory.IAction;
import com.github.aiderpmsi.pims.treebrowser.actions.AssignFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.ExecuteFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.GotoFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.SwitchFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.TreeFactory;
import com.github.aiderpmsi.pims.treemodel.Node;
import com.github.aiderpmsi.pims.treemodel.TreeContentHandler;

public class TreeBrowserFactory {

	private final String resource;

	private final Supplier<JexlEngine> jexlEngineSupplier;

	private final LinkedList<ActionDefinition> actionsDefinition = new LinkedList<>();
	
	public TreeBrowserFactory(final String resource, final Supplier<JexlEngine> jexlEngineSupplier) {
		this.resource = resource;
		this.jexlEngineSupplier = jexlEngineSupplier;
		
		// FILLS THE LIST OF ACTION DEFINITIONS
		for (final Object[] dfltaction : dfltactions) {
			addActionDefinition(new ActionDefinition(
					(String) dfltaction[0], (String) dfltaction[1], (IActionFactory) dfltaction[2]));
		}

	}
	
	public Node<?> newTree() throws TreeBrowserException {
		// CREATES THE JEXL ENGINE
		final JexlEngine je = jexlEngineSupplier.get();
				
		// CREATES THE ACTION TREE
		try (Reader reader = Files.newBufferedReader(
				Paths.get(this.getClass().getClassLoader().getResource(resource).toURI()), Charset.forName("UTF-8"))) {
			return createTree(reader, actionsDefinition, je);
		} catch (IOException | SAXException | URISyntaxException e) {
			throw new TreeBrowserException(e);
		}
	}

	private Node<IAction> createTree(final Reader reader,
			final List<ActionDefinition> actionDefinitions,
			final JexlEngine je) throws SAXException, IOException {
		// CREATES THE CONTENTHANDLER FOR TREE GENERATION
		final TreeContentHandler tc = new TreeContentHandler();
		
		// SETS THE ACTIONS DEFINITION FROM LIST OF ACTION DEFINITION
		for (final ActionDefinition actionDefinition : actionDefinitions) {
			tc.addAction(actionDefinition.nameSpace, actionDefinition.command, actionDefinition.actionFactory);
		}
		
		// SETS THE JEXLENGINE
		tc.setEngine(je);
		
		// CREATES THE XML READER
		final XMLReader saxReader = XMLReaderFactory.createXMLReader();
        saxReader.setContentHandler(tc);

        // PARSES THE TREE DEFINITION
        saxReader.parse(new InputSource(reader));

        // RETURNS THE TREE OF ACTIONS
        return tc.getTree();
	}
	
	public void addActionDefinition(final ActionDefinition actionDefinition) {
		actionsDefinition.add(actionDefinition);
	}
	
	public class ActionDefinition {
		public final String nameSpace, command;
		public final IActionFactory actionFactory;
		
		public ActionDefinition(final String nameSpace, final String command, final IActionFactory actionFactory) {
			this.nameSpace = nameSpace;
			this.command = command;
			this.actionFactory = actionFactory;
		}
		
	}
	
	/** Default actions */
	private static final Object[][] dfltactions = {
			{"http://default.actions/default", "execute", new ExecuteFactory()},
			{"http://default.actions/default", "assign", new AssignFactory()},
			{"http://default.actions/default", "switch", new SwitchFactory()},
			{"http://default.actions/default", "goto", new GotoFactory()},
			{"", "tree", new TreeFactory()}
	};
}
