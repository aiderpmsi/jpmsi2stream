package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public abstract class Action {

	static final Pattern pparent = Pattern.compile("^parent\\((\\d+)\\)$");
	static final Pattern pnegsibling = Pattern.compile("^sibling\\((-\\d+)\\)$");
	static final Pattern psibling = Pattern.compile("^sibling\\((\\+?\\d+)\\)$");
	static final Pattern pchild = Pattern.compile("^child\\((\\d+)\\)$");
	
	public Node execute(Node node, JexlContext jc, JexlEngine jexl, Argument[] arguments) throws IOException {
		// EXECUTES THE ACTION
		String result = executeAction(node, jc, jexl, arguments);
		// DEPENDING ON THE RESULT, MOVE INTO DOCUMENT
		String[] movesor = result.split("\\|"), movesand;
		Matcher m;
		Node initialNode = node;
		for (String moveor : movesor) {
			movesand = moveor.split("/");
			node = initialNode;
			for (String moveand : movesand) {
				if ((m = pparent.matcher(moveand)).matches()) {
					node = parent(node, new Integer(m.group(1)));
				} else if ((m = pnegsibling.matcher(moveand)).matches()) {
					node = negsibling(node, new Integer(m.group(1)));
				} else if ((m = psibling.matcher(moveand)).matches()) {
					node = sibling(node, new Integer(m.group(1)));
				} else if ((m = pchild.matcher(moveand)).matches()) {
					node = child(node, new Integer(m.group(1)));
				} else {
					throw new RuntimeException(moveand + " is not a possible move for Action (" + result + ")");
				}
			}
			if (node != null)
				break;
		}
		
		return node;
	}
	
	public abstract String executeAction(Node node, JexlContext jc, JexlEngine jexl, Argument[] arguments) throws IOException ;

	private Node nextElement(Node node) {
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getNextSibling();
		}
		return node;
	}

	private Node previousElement(Node node) {
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getPreviousSibling();
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

	private Node negsibling(Node node, Integer nb) {
		for (int i = 0 ; i > nb ; i--) {
			if (node == null) break;
			node = previousElement(node.getPreviousSibling());
		}
		return node;
	}

	private Node child(Node node, Integer nb) {
		for (int i = 0 ; i < nb ; i++) {
			if (node == null) break;
			node = nextElement(node.getFirstChild());
		}
		return node;
	}
}
