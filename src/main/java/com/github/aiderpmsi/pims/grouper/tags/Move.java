package com.github.aiderpmsi.pims.grouper.tags;

import java.io.IOException;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public class Move extends BaseAction {

	public String executeAction(Node node, JexlContext jc, JexlEngine jexl, Argument[] args) throws IOException {
		// GETS ARGUMENTS
		String dest = "";
		for (Argument arg : args) {
			switch (arg.key) {
			case "dest":
				dest = arg.value; break;
			default:
				throw new IOException("Argument " + arg.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		return dest;
	}

}
