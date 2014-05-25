package com.github.aiderpmsi.pims.treebrowser;

import java.util.HashMap;

import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Document;

public interface Config extends Cloneable {

	public abstract HashMap<String, Object> getContext();
	
	public abstract JexlEngine getJexlEngine();
	
	public abstract Document getTree();
	
	public abstract Long getClonedTime();

	public Object clone();
	
}
