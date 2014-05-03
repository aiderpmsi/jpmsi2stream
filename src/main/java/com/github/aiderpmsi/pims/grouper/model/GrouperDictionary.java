package com.github.aiderpmsi.pims.grouper.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

public class GrouperDictionary {

	private static final String configPath = "groupdefs.xml";
	private static final String configXslPath = "groupdefsset.xsl";
	
	private Map<String, Set<String>> instances =
			new HashMap<>();
	
	public synchronized Set<String> getInstance(String group) {
		Set<String> instance = instances.get(group);
        
		// IF GROUP DEFINITION DOES NOT EXIST, CREATE IT
		if (instance == null) {
			
			try {
				instance = new HashSet<>(createInstance(group).getNames());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			instances.put(group, instance);
		}
		
		return instance;
	}
	
	private Group createInstance(String group) throws IOException, JAXBException, TransformerException {
		// Opens the config file
		InputStream configStream = 
				GrouperDictionary.class.getClassLoader().getResourceAsStream(configPath);
				
		// Opens the thansformation
		InputStream configXslStream = 
				GrouperDictionary.class.getClassLoader().getResourceAsStream(configXslPath);
				
		TransformerFactory tFactory = org.apache.xalan.processor.TransformerFactoryImpl.newInstance();
		Transformer transformer = tFactory.newTransformer(
				new StreamSource(configXslStream));
		
		// SETS THE GROUP WE HAVE TO GET
		transformer.setParameter("group", group);
				
		// INPUT STREAMSOURCE
		StreamSource inp = new StreamSource(configStream);
	
		// JAXB OBJECT
		JAXBContext jaxbContext = JAXBContext.newInstance(Group.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			    			    
		// Creates a config object from the result
		JAXBResult jaxbResult = new JAXBResult(jaxbUnmarshaller);
		transformer.transform(inp, jaxbResult);

		return (Group) jaxbResult.getResult();
	} 

}
