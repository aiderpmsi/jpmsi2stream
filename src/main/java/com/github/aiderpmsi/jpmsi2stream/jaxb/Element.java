package com.github.aiderpmsi.jpmsi2stream.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="element")
public class Element {

	@XmlElement(name="name")
	private String name = null;
	
	@XmlElement(name="pattern")
	private String pattern = null;
	
	@XmlElement(name="in", defaultValue="")
	private String in = null;
	
	@XmlElement(name="out", defaultValue="")
	private String out = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in = in;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

}
