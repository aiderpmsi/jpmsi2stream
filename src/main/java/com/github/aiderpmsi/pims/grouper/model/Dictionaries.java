package com.github.aiderpmsi.pims.grouper.model;

import java.util.EnumMap;

public class Dictionaries {

	private EnumMap<SimpleDictionary.Type, SimpleDictionary> dictionaries =
			new EnumMap<>(SimpleDictionary.Type.class);
	

	public SimpleDictionary get(SimpleDictionary.Type type) {
		SimpleDictionary dico;
		if ((dico = dictionaries.get(type)) == null) {
			dico = new SimpleDictionary(type);
			dictionaries.put(type, dico);
		}
		return dico;
	}
}
