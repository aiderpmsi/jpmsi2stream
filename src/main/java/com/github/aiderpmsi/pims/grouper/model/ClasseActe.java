package com.github.aiderpmsi.pims.grouper.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="class")
public class ClasseActe {

	private String id = null;
	
	private List<String> actes = null;

	@XmlElementWrapper(name = "actes")
	@XmlElement(name="acte")
	public List<String> getActes() {
		return actes;
	}

	public void setActes(List<String> actes) {
		this.actes = actes;
	}

	@XmlElement(name="acte")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
