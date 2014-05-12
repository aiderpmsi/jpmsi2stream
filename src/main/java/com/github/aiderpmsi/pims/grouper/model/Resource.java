package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashMap;


public enum Resource {

	UNCLASSIFIED("unclassified"), ACTECLASSANT("acteclassant"), CLASSEACTE("classeacte"),
	ACTEMINEURCHIRRECLASSANT("actemineurchirreclassant"), ACTECLASSANTOP("acteclassantop");
	
	private static HashMap<String, Resource> resourcesMap = new HashMap<>();
	
	static {
		for (Resource value : Resource.values()) {
			resourcesMap.put(value.getName(), value);
		}
	}
	
	private String name;

	private Resource(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static Resource createResource(String resourceName) {
		return resourcesMap.get(resourceName);
	}
	
}
