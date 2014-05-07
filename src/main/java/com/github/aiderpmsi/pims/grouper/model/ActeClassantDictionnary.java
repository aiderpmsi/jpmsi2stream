package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashSet;
import java.util.Set;

public class ActeClassantDictionnary extends BaseAbstractDictionary<Set<String>, ActeClassant> {

	public class Mapper implements JAXBMapper<ActeClassant, Set<String>> {
		@Override
		public Set<String> transform(ActeClassant model) {
			return new HashSet<>(model.getActes());
		}
	}
	
	public ActeClassantDictionnary() {
		// SETS THE JAXBMAPPER AND JAXBCLASS
		setJaxbMapper(new Mapper());
		setJaxbClass(ActeClassant.class);
	}

	@Override
	public String getConfigPath() {
		return "grouper-acteclassant.xml";
	}
	
	@Override
	public String getConfigXslPath() {
		return "grouper-acteclassant-set.xsl";
	}

	@Override
	public String getKeyParameterInXsl() {
		return "cmd";
	}

}
