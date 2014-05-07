package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashSet;
import java.util.Set;

public class UnclassifiedDictionary extends BaseAbstractDictionary<Set<String>, Unclassified> {

	public class Mapper implements JAXBMapper<Unclassified, Set<String>> {
		@Override
		public Set<String> transform(Unclassified model) {
			return new HashSet<>(model.getNames());
		}
	}
	
	public UnclassifiedDictionary() {
		// SETS THE JAXBMAPPER AND JAXBCLASS
		setJaxbMapper(new Mapper());
		setJaxbClass(Unclassified.class);
	}

	@Override
	public String getConfigPath() {
		return "grouper-unclassified.xml";
	}
	
	@Override
	public String getConfigXslPath() {
		return "grouper-unclassified-set.xsl";
	}

	@Override
	public String getKeyParameterInXsl() {
		return "group";
	}

}
