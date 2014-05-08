package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashSet;
import java.util.Set;

public class ClasseActeDictionary extends BaseAbstractDictionary<Set<String>, ClasseActe> {

	public class Mapper implements JAXBMapper<ClasseActe, Set<String>> {
		@Override
		public Set<String> transform(ClasseActe model) {
			return new HashSet<>(model.getActes());
		}
	}
	
	public ClasseActeDictionary() {
		// SETS THE JAXBMAPPER AND JAXBCLASS
		setJaxbMapper(new Mapper());
		setJaxbClass(ClasseActe.class);
	}

	@Override
	public String getConfigPath() {
		return "grouper-classeacte.xml";
	}
	
	@Override
	public String getConfigXslPath() {
		return "grouper-classeacte-set.xsl";
	}

	@Override
	public String getKeyParameterInXsl() {
		return "classe";
	}

}
