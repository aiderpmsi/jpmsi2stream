package com.github.aiderpmsi.pims.grouper.tags;

import java.io.IOException;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public class Move extends BaseAction {

	private String dest;
	
	public void setDest(String dest) {
		this.dest = dest;
	}

	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl)
			throws IOException {
		return dest;
	}

	@Override
	public void init() {
		dest = "";
	}

	@Override
	public void cleanout() {
		// DO NOTHING
	}

}
