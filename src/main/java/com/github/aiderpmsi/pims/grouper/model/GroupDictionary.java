package com.github.aiderpmsi.pims.grouper.model;

import java.util.HashSet;
import java.util.Set;

public class GroupDictionary extends BaseDictionary<Set<String>, Group> {

	public class Mapper implements JAXBMapper<Group, Set<String>> {
		@Override
		public Set<String> transform(Group model) {
			return new HashSet<>(model.getNames());
		}
	}
	
	public GroupDictionary() {
		// SETS THE JAXBMAPPER AND JAXBCLASS
		setJaxbMapper(new Mapper());
		setJaxbClass(Group.class);
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
