package com.github.aiderpmsi.pims.grouper.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public abstract class BaseAbstractDictionary<T, U> {

	public abstract String getConfigPath();
	public abstract String getConfigXslPath();
	public abstract String getKeyParameterInXsl();
	
	protected Map<String, T> dictionnary =
			new HashMap<>();
	
	protected JAXBMapper<U, T> jaxbMapper;
	
	protected Class<U> jaxbClass;
	
	public T getDefintion(String key) {
		T definition = dictionnary.get(key);
        
		// IF KEY DEFINITION DOES NOT EXIST, CREATE IT AND LOAD IT
		if (definition == null) {
			
			try {
				definition = createDefinition(key);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			dictionnary.put(key, definition);
		}
		
		return definition;
	}
	
	protected T createDefinition(String key) throws IOException, JAXBException, TransformerException {
		// OPENS THE CONFIG FILE
		InputStream configStream = 
				BaseAbstractDictionary.class.getClassLoader().getResourceAsStream(getConfigPath());
				
		// Opens the thansformation
		InputStream configXslStream = 
				BaseAbstractDictionary.class.getClassLoader().getResourceAsStream(getConfigXslPath());
				
		TransformerFactory tFactory = org.apache.xalan.processor.TransformerFactoryImpl.newInstance();
		Transformer transformer = tFactory.newTransformer(
				new StreamSource(configXslStream));
		
		// SETS THE XSL PARAMETERS
		transformer.setParameter(getKeyParameterInXsl(), key);

		// INPUT STREAMSOURCE
		StreamSource inp = new StreamSource(configStream);
	
		// JAXB OBJECT
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbClass);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			    			    
		// CREATES THE JAXBOBJECT
		JAXBResult jaxbResult = new JAXBResult(jaxbUnmarshaller);
		transformer.transform(inp, jaxbResult);

		// INPUT STREAMSOURCE
		StreamSource inp2 = new StreamSource(BaseAbstractDictionary.class.getClassLoader().getResourceAsStream(getConfigPath()));
		transformer.transform(inp2, new StreamResult(new File("/tmp/tex.xml")));

		@SuppressWarnings("unchecked")
		U jaxbObject = (U) jaxbResult.getResult();
		
		// TRANSFORMS THE JAXBOBJECT TO DEFINITION OBJECT
		T definition = jaxbMapper.transform(jaxbObject);
		
		return definition;
	}

	public void setJaxbMapper(JAXBMapper<U, T> jaxbMapper) {
		this.jaxbMapper = jaxbMapper;
	}

	public void setJaxbClass(Class<U> jaxbClass) {
		this.jaxbClass = jaxbClass;
	} 

}
