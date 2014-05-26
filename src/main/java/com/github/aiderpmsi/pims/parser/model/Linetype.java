package com.github.aiderpmsi.pims.parser.model;

import java.util.List;

public class Linetype {

	private String name = null;
	
	private List<Element> elements = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	
}
