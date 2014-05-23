package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public interface Action {
	
	public class Argument{public String key, value;};
	
	public Node execute(Node node, JexlContext jc, JexlEngine jexl, Argument[] arguments) throws IOException;

}
