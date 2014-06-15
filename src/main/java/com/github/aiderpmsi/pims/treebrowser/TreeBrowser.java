package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.github.aiderpmsi.pims.treebrowser.actions.Argument;

public class TreeBrowser {

	/** jexl2 context map */
	private JexlContext jc = null;

	/** jexl2 engine */
	private JexlEngine jexl = null;
    
	/** associations between namespaces and actions */
	private HashMap<String, HashMap<String, Action>> actions = new HashMap<>();

	/** associates namespaces prefixes with namespaces */
	private HashMap<String, String> namespaces = new HashMap<>();
	
	/** Dom document used as tree source */
	private Document tree = null;

	/** Default actions */
	private static final Object[][] dfltactions = {
			{"http://default.actions/default", "execute", new Execute()},
			{"http://default.actions/default", "assign", new Assign()},
			{"http://default.actions/default", "switch", new Switch()},
			{"http://default.actions/default", "move", new Move()},
	};
	
	/**
	 * Adds an action for a namespace
	 * @param namespace
	 * @param namecommand
	 * @param action
	 */
	public void addAction(String namespace, String namecommand, Action action) {
		HashMap<String, Action> to_add;
		if ((to_add = actions.get(namespace)) == null) {
			to_add = new HashMap<>();
			actions.put(namespace, to_add);
		}
		to_add.put(namecommand, action);
	}

	private void initiateNamespaces() {
		// GO TO ROOT
		Node node = tree.getDocumentElement();
		
		// GETS ATTRIBUTES
		NamedNodeMap atts = node.getAttributes();
		
		// GETS ALL NAMESPACES
		Node att;
		String attName;
		String prefix;
		for (int i = 0 ; i < atts.getLength() ; i++) {
			 att = atts.item(i);
			 attName = att.getNodeName();
			 if (attName.startsWith("xmlns")) {
				 // THIS ATTRIBUTE DEFINES A NAMESPACE, WE HAVE TO ASSOCIATE THE PREFIX WITH THE NAMESPACE
				 if (attName.length() == 5) {
					 prefix = "";
				 } else {
					 prefix = attName.substring(6);
				 }
				 namespaces.put(prefix, att.getNodeValue());
			 }
		}
	}
	
	public void go() throws IOException {
		// ADD DEFAULT ACTIONS
		for (Object[] dfltaction : dfltactions) {
			addAction((String) dfltaction[0], (String) dfltaction[1], (Action) dfltaction[2]);
		}
		
		// INITIATE NAMESPACES
		initiateNamespaces();
		
		// CURRENT ELEMENT
		Node node;
		
		// GO TO FIRST CHILD AND EXECUTE IT
		node = tree.getDocumentElement().getFirstChild();

		String localname, namespace, namespaceURI;
		HashMap<String, Action> nsactions;
		Action action;
		NamedNodeMap atts;
		Node att;
		Argument[] args;
		machine : while (node != null) {
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				// WE CANNOT WORK ON TEXT NODES, GO TO NEXT SIBLING
				node = node.getNextSibling();
				continue machine;
			} else {
				// SPLIT ELEMENT NAME BETWEEN NAMESPACE AND VALUE
				localname = "";
				namespace = "";
				String[] elements = node.getNodeName().split(":", 2);
				if (elements.length == 1) {
					localname = elements[0];
				} else {
					namespace = elements[0];
					localname = elements[1];
				}
				// GETS THE CLASS EXECUTING THIS ACTION
				if ((namespaceURI = namespaces.get(namespace)) == null)
					throw new IOException("namespace '" + namespace + "' is unknown");	
				if ((nsactions = actions.get(namespaceURI)) == null)
					throw new IOException("namespace '" + namespace + "' is not defined");
				if ((action = nsactions.get(localname)) == null)
					throw new IOException(localname + " function in namespace " + namespace + " is unknown");
				// SETS THE ARGUMENTS OF THE ACTION CLASS
				atts = node.getAttributes();
				args = new Argument[atts.getLength()];
				for (int i = 0 ; i < args.length ; i++) {
					att = atts.item(i);
					args[i] = new Argument();
					args[i].key = att.getNodeName();
					args[i].value = att.getNodeValue();
				}
				// EXECUTES THE ACTION AND MOVES THE NODE CURRENT
				node = action.execute(node, jc, jexl, args);
			}
		}
		
	}    
	
	public JexlContext getJc() {
		return jc;
	}

	public void setJc(JexlContext jc) {
		this.jc = jc;
	}

	public JexlEngine getJexl() {
		return jexl;
	}

	public void setJexl(JexlEngine jexl) {
		this.jexl = jexl;
	}

	public Document getTree() {
		return tree;
	}

	public void setTree(Document tree) {
		this.tree = tree;
	}

}
