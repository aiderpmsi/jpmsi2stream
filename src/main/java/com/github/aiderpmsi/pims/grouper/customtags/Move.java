package com.github.aiderpmsi.pims.grouper.customtags;

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
		// TODO Auto-generated method stub
		return dest;
	}

}
