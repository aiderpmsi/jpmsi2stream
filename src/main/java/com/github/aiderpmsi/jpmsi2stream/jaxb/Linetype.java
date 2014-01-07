package com.github.aiderpmsi.jpmsi2stream.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="linetype")
public class Linetype {

	@XmlElement(name = "name")
	private String name = null;
	
	@XmlElement(name="elements")
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
