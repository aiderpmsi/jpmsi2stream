package com.github.aiderpmsi.pims.linestypes;

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
import javax.xml.transform.stream.StreamSource;

import com.github.aiderpmsi.pims.jaxb.Linetype;

public class LineDictionary {

	private static final String configPath = "linedefs.xml";
	private static final String configXslPath = "linedefsset.xsl";
	
	private Map<String, PmsiLineType> instances =
			new HashMap<String, PmsiLineType>();
	
	public synchronized PmsiLineType getInstance(String element) {
		// Get the singleton instance
		PmsiLineType instance = instances.get(element);
        
		if (instance == null) {
			
			try {
				instance = createInstance(element);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			instances.put(element, instance);
		}
		
		return instance;
	}
	
	private static PmsiLineType createInstance(String element) throws IOException, JAXBException, TransformerException {
			// If eof is asked
			if (element.equals("eof"))
				return new EndOfFile();
			else {
				// Opens the config file
				InputStream configStream = 
						LineDictionary.class.getClassLoader().getResourceAsStream(configPath);
				
				// Opens the thansformation
				InputStream configXslStream = 
						LineDictionary.class.getClassLoader().getResourceAsStream(configXslPath);
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer(
						new StreamSource(configXslStream));
				// Sets the kind of line we have to parse
				transformer.setParameter("linetype", element);
				
				// Input StreamSource
				StreamSource inp = new StreamSource(configStream);
	
				// Jaxb object
				JAXBContext jaxbContext = JAXBContext.newInstance(Linetype.class);
			    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			    
			    // transformer.transform(inp,  new StreamResult(System.out));
			    
				// Creates a config object from the result
			    JAXBResult jaxbResult = new JAXBResult(jaxbUnmarshaller);
			    transformer.transform(inp, jaxbResult);
			    return new PmsiLineTypeImpl((Linetype) jaxbResult.getResult());
			} 

	}
}
