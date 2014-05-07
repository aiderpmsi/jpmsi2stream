package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashSet;
import java.util.Set;

public class ActesClassantsDictionnary extends BaseDictionary<Set<String>, ActesClassants> {

	public class Mapper implements JAXBMapper<ActesClassants, Set<String>> {
		@Override
		public Set<String> transform(ActesClassants model) {
			return new HashSet<>(model.getActes());
		}
	}
	
	public ActesClassantsDictionnary() {
		// SETS THE JAXBMAPPER AND JAXBCLASS
		setJaxbMapper(new Mapper());
		setJaxbClass(ActesClassants.class);
	}

	@Override
	public String getConfigPath() {
		return "actesclassants.xml";
	}
	
	@Override
	public String getConfigXslPath() {
		return "groupactesclassantsset.xsl";
	}

	@Override
	public String getKeyParameterInXsl() {
		return "cmd";
	}

}
