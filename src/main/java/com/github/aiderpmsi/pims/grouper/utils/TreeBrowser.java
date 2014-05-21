package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TreeBrowser {

	// JEXL2 CONTEXT
	private JexlContext jc = new MapContext() {{
		set("continue", new Boolean(true));
	}};

	// CREATE A JEXL2ENGINE
	private JexlEngine jexl = new JexlEngine();
    
    // ASSOCIATES NAMESPACES WITH ACTIONS
	private HashMap<String, HashMap<String, Class<? extends Action>>> actions = new HashMap<>();

	// ASSOCIATES NAMESPACES PREFIXES WITH NAMESPACES
	private HashMap<String, String> namespaces = new HashMap<>();
	
    // DOM DOCUMENT
	private Document document = null;

	public void setDOM(Document document) {
		this.document = document;
	}
	public void addDataModel(String key, Object value) {
		jc.set(key, value);
	}

	public Object getDataModel(String key) {
		return jc.get(key);
	}
	
	public void AddAction(String namespace, String namecommand, Class<? extends Action> action) {
		HashMap<String, Class<? extends Action>> to_add;
		if ((to_add = actions.get(namespace)) == null) {
			to_add = new HashMap<>();
			actions.put(namespace, to_add);
		}
		to_add.put(namecommand, action);
	}
	
	public void go() throws IOException {
		// CURRENT ELEMENT
		Node current;
		
		// GO TO ROOT
		current = document.getDocumentElement();

		// INITIATE NAMESPACES
		NamedNodeMap attributes = current.getAttributes();
		for (int i = 0 ; i < attributes.getLength() ; i++) {
			Node attribute = attributes.item(i);	

			if (attribute.getNodeName().startsWith("xmlns:")) {
				// THIS ATTRIBUTE DEFINES A NAMESPACE
				// ASSOCIATE THIS NAMESPACE WITH ONE LIST OF ACTIONS
				namespaces.put(attribute.getNodeName().substring(6), attribute.getNodeValue());
			} else if (attribute.getNodeName().startsWith("xmlns")) {
				// THIS ATTRIBUTE DEFINES THe DEFAULT NAMESPACE
				namespaces.put("", attribute.getNodeValue());
			}
			else {
				// THIS ATTRIBUTE IS UNKNOWN, THROW AN ERROR
				throw new IOException("Unknow attribute " + attribute.getNodeName() + " at " + attribute.getParentNode().getNodeName() + " element");
			}
		}
		
		// GO TO FIRST CHILD AND EXECUTE IT
		current = current.getFirstChild();

		machine : while ((Boolean) jc.get("continue") == true) {
			if (current == null) {
				// BROWSER CONTINUES TO WORK, BUT NORTHING TO WORK ON
				throw new IOException("Browser wants to continue but has finished xml definition");
			} else if (current.getNodeType() == Node.TEXT_NODE) {
				// WE CANNOT WORK ON TEXT NODES, GO TO NEXT SIBLING
				current = current.getNextSibling();
				continue machine;
			} else if (current instanceof Element) {
				// WE WORK ONLY ON ELEMENTS
				Element element = (Element) current;
				// SPLIT ELEMENT NAME BETWEEN NAMESPACE AND VALUE
				String localname = "", namespace = "";
				String[] elements = element.getNodeName().split(":", 2);
				if (elements.length == 1) {
					localname = elements[0];
				} else {
					namespace = elements[0];
					localname = elements[1];
				}
				// GETS THE CLASS EXECUTING THIS ACTION
				String namespaceURI = namespaces.get(namespace);
				if (namespaceURI == null)
					throw new IOException("namespace '" + namespace + "' is unknown");				
				Class<? extends Action> actionClass = actions.get(namespaceURI).get(localname);
				if (actionClass == null)
					throw new IOException(localname + " function in namespace " + namespace + " is unknown");
				try {
					Action action = actionClass.newInstance();
					// SETS THE ARGUMENTS OF THIS CLASS
					attributes = element.getAttributes();
					for (int i = 0 ; i < attributes.getLength() ; i++) {
						Node attribute = attributes.item(i);
						String attributeName = attribute.getNodeName();
						Method setMethod = actionClass.getMethod(
								"set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1),
								String.class);
						setMethod.invoke(action, attribute.getNodeValue());
					}
					// EXECUTES THE ACTION AND MOVES THE CURSOR
					current = action.execute(current, jc, jexl);
				} catch (IllegalAccessException | NoSuchMethodException | SecurityException | InstantiationException | IllegalArgumentException | InvocationTargetException | DOMException e) {
					throw new IOException(e);
				}
			} else {
				throw new RuntimeException("Fatal error in " + getClass().toString());
			}
		}
		
	}    
	
}
