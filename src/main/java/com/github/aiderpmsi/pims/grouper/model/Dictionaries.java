package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashMap;

public class Dictionaries {

	private HashMap<String, SimpleDictionary> dictionnaries = new HashMap<>();
	
	String prefix, suffix;
	
	public Dictionaries(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	public SimpleDictionary get(String resourceName) {
		SimpleDictionary dico = null;
		if ((dico = dictionnaries.get(resourceName)) == null) {
			dico = new SimpleDictionary(prefix + resourceName + suffix);
			dictionnaries.put(resourceName, dico);
		}
		return dico;
	}
}
