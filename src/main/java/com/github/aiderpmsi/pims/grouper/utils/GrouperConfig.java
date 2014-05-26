package com.github.aiderpmsi.pims.grouper.utils;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Document;

import com.github.aiderpmsi.pims.treebrowser.Config;

public class GrouperConfig implements Config {

	private Document tree = null;

	private Long clonedTime = null;

	private JexlEngine jexlEngine = null;

	private HashMap<String, Object> context = null;
	
	@Override
	public HashMap<String, Object> getContext() {
		return context;
	}

	@Override
	public JexlEngine getJexlEngine() {
		return jexlEngine;
	}

	@Override
	public Document getTree() {
		return tree;
	}

	@Override
	public Long getClonedTime() {
		return clonedTime;
	}

	public void setTree(Document tree) {
		this.tree = tree;
	}

	public void setClonedTime(Long clonedTime) {
		this.clonedTime = clonedTime;
	}

	public void setJexlEngine(JexlEngine jexlEngine) {
		this.jexlEngine = jexlEngine;
	}

	public void setContext(HashMap<String, Object> context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		GrouperConfig cloned = new GrouperConfig();
		Long cloningDate = new Date().getTime();
		cloned.setClonedTime(cloningDate);
		setClonedTime(cloningDate);
		cloned.setContext((HashMap<String, Object>) getContext().clone());
		cloned.setJexlEngine(getJexlEngine());
		cloned.setTree((Document) getTree().cloneNode(true));
		return cloned;
	}
}
