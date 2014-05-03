package com.github.aiderpmsi.pims.grouper.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="group")
public class Group {

	private List<String> names = null;

	@XmlElementWrapper(name = "elements")
	@XmlElement(name="element")
	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
	
}
