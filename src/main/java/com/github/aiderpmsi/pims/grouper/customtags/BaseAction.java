package com.github.aiderpmsi.pims.grouper.customtags;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

import com.github.aiderpmsi.pims.grouper.utils.Action;

public abstract class BaseAction implements Action {

	static final Pattern pparent = Pattern.compile("^parent\\((\\d+)\\)$");
	static final Pattern psibling = Pattern.compile("^sibling\\((\\d+)\\)$");
	
	@Override
	public Node execute(Node node, JexlContext jc, JexlEngine jexl) throws IOException {
		// EXECUTES THE ACTION
		String result = executeAction(node, jc, jexl);
		// DEPENDING ON THE RESULT, MOVE INTO DOCUMENT
		if (result.equals("this")) {
			return node;
		} else if (result.equals("child|sibling")) {
			// WE HAVE TO GO INSIDE NEXT CHILD OR SIBLING DEPENDING ON WHAT EXISTS
			// FIRST FIND THE FIRST ELEMENT CHILD
			Node nextNode = nextElement(node.getFirstChild());
			if (nextNode != null)
				return nextNode;
			// IF THE FIRST ELEMENT CHILD DOESN'T EXIST, FIND THE FIRST SIBLING
			nextNode = nextElement(node.getNextSibling());
				return nextNode;
		} else if (result.equals("child")) {
			return nextElement(node.getFirstChild());
		} else if (result.equals("sibling")) {
			return nextElement(node.getNextSibling());
		} else {
			// WE HAVE TO EXECUTE THE COMMANDS
			String[] results = result.split("/");
			for (String resultelt : results) {
				Matcher m;
				if ((m = pparent.matcher(resultelt)).matches()) {
					node = parent(node, new Integer(m.group(1)));
				} else if ((m = psibling.matcher(resultelt)).matches()) {
					node = sibling(node, new Integer(m.group(1)));
				} else {
					throw new RuntimeException(resultelt + " is not a possible result for BaseAction (" + result + ")");
				}
			}
			return node;
		}
	}
	
	public abstract String executeAction(Node node, JexlContext jc, JexlEngine jexl) throws IOException ;

	private Node nextElement(Node node) {
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getNextSibling();
		}
		return node;
	}

	private Node parent(Node node, Integer nb) {
		for (int i = 0 ; i < nb ; i++) {
			if (node == null) break;
			node = nextElement(node.getParentNode());
		}
		return node;
	}
	
	private Node sibling(Node node, Integer nb) {
		for (int i = 0 ; i < nb ; i++) {
			if (node == null) break;
			node = nextElement(node.getNextSibling());
		}
		return node;
	}
}
