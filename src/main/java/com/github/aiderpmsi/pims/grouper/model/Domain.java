package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashMap;

public enum Domain {

	MAIN("main"), ACTE("acte"), DA("da"), DAD("dad");
		
	private static HashMap<String, Domain> domainsMap = new HashMap<>();
		
	static {
		for (Domain value : Domain.values()) {
			domainsMap.put(value.getName(), value);
		}
	}

	private String name;

	private Domain(String name) {
		this.name = name;
	}
		
	public String getName() {
		return name;
	}
		
	public static Domain createResource(String domainName) {
		return domainsMap.get(domainName);
	}
		
}
