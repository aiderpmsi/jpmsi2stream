package com.github.aiderpmsi.pims.treemodel;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.jexl2.JexlEngine;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.Argument;

public class TreeContentHandler implements ContentHandler {

	/** Index on ids */
	private HashMap<String, Node<?>> indexId = new HashMap<>();
	
	/** Tree representation */
	private Node<Action> root = null;
	
	/** Working Nodes From precedent levels */
	private LinkedList<Node<?>> nodes = new LinkedList<>();
	
	/** Current level */
	private int level = 0;
	
	/** Script Engine */
	private JexlEngine je = null;
	
	/** associations between namespaces and actions */
	private HashMap<String, HashMap<String, ActionFactory<? extends Action>>> actions = new HashMap<>();

	/**
	 * Adds an action for a namespace
	 * @param namespace
	 * @param namecommand
	 * @param action
	 */
	public void addAction(String namespace, String namecommand, ActionFactory<? extends Action> actionFactory) {
		HashMap<String, ActionFactory<? extends Action>> actionFactories;
		if ((actionFactories = actions.get(namespace)) == null) {
			actionFactories = new HashMap<>();
			actions.put(namespace, actionFactories);
		}
		actionFactories.put(namecommand, actionFactory);
	}

	public void setEngine(JexlEngine je) {
		this.je = je;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// DO NOTHING (CHARACTERS ARE NOT USED)
	}

	@Override
	public void endDocument() throws SAXException {
		// DO NOTHING
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// JUST LET LEVELS STAY LESS OR EQUAL TO SIZE OF NODES
		if (nodes.size() > level) {
			nodes.removeLast();
		}
		level--;
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// DO NOTHING
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// DO NOTHING
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// DO NOTHING
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// DO NOTHING
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// DO NOTHING
	}

	@Override
	public void startDocument() throws SAXException {
		// DO NOTHING
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		// CREATES THE CORRESPONDING NODE
		Node<Action> newNode = new Node<>(Action.class, indexId);

		// GETS THE ACTION FACTORY FOR THIS CLASS
		HashMap<String, ActionFactory<? extends Action>> actionFactories = null;
		ActionFactory<? extends Action> actionFactory = null;
		if ((actionFactories = actions.get(uri)) == null)
			throw new SAXException("namespace '" + uri + "' is not defined");
		if ((actionFactory = actionFactories.get(localName)) == null)
			throw new SAXException(localName + " function in namespace " + uri + " is unknown");

		// SETS THE ARGUMENTS OF THE ACTION CLASS
		Argument[] args = new Argument[atts.getLength()];
		for (int i = 0 ; i < args.length ; i++) {
			args[i] = new Argument();
			args[i].key = atts.getLocalName(i);
			args[i].value = atts.getValue(i);
			
			// IF THIS ELEMENT HAS AN ID, USE IT TO INDEX IT
			if (args[i].key.equals("id")) {
				indexId.put(args[i].value, newNode);
			}
		}
		
		// GETS THE CORRESPONDING ACTION
		try {
			newNode.setContent(actionFactory.createAction(je, args));
		} catch (IOException e) {
			throw new SAXException(e);
		}
				
		// IF WE ARE AT ROOT LEVEL, STORE THIS NODE AS THE ROOT NODE
		if (root == null) {
			root = newNode;
		}		
		// INCREMENTS LEVEL
		level++;
		
		// IF WE ARE AT THE START, ADD THIS ELEMENT
		if (nodes.size() == 0) {
			nodes.add(newNode);
		}
		// IF WE HAVE THE SAME NUMBER OF NODES THAN LEVEL, IT MEANS WE HAVE A SIBLING
		else if (nodes.size() == level) {
			// ADD THIS NODE AS A SIBLING
			nodes.removeLast().nextSibling = newNode;
			nodes.addLast(newNode);
		}
		// If NOT, IT MEANS WE HAVE A FIRST CHILD
		else {
			nodes.getLast().firstChild = newNode;
			nodes.addLast(newNode);
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// DO NOTHING
	}

	public Node<Action> getTree() {
		return root;
	}

	public HashMap<String, Node<?>> getIndexId() {
		return indexId;
	}

}
