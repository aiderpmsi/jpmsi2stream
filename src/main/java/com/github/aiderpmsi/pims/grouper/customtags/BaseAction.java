package com.github.aiderpmsi.pims.grouper.customtags;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

import com.github.aiderpmsi.pims.grouper.utils.Action;

public abstract class BaseAction implements Action {

	static final Pattern p = Pattern.compile("^parent\\((\\d+)\\)");
	
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
			// WE HAVE TO FIND IF WE HAVE TO GO TO PARENTS
			Matcher m = p.matcher(result);
			if (m.matches()) {
				Integer nbparents = new Integer(m.group(1));
				for (int i = 0 ; i < nbparents ; i++) {
					node = node.getParentNode();
					if (node == null)
						return null;
				}
				// FIND NEXT SIBLING ELEMENT OF THIS PARENT NODE
				return nextElement(node);
			} else {
				throw new RuntimeException(result + " is not a possible result for BaseAction");
			}
		}
	}
	
	public abstract String executeAction(Node node, JexlContext jc, JexlEngine jexl) throws IOException ;

	private Node nextElement(Node node) {
		while (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE)
				return node;
			else
				node = node.getNextSibling();
		}
		return node;
	}
}
