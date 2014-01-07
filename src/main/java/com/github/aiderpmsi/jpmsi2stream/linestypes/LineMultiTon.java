package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.io.IOException;
import java.net.URL;
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

import com.github.aiderpmsi.jpmi2stream.utils.ClasspathHandler;
import com.github.aiderpmsi.jpmsi2stream.jaxb.Linetype;

public class LineMultiTon {

	private static final String configUrl = "classpath:linedefs.xml";
	private static final String configXslUrl = "classpath:linedefsset.xsl";
	
	private static final Map<String, PmsiLineType> instances =
			new HashMap<String, PmsiLineType>();
	
	private LineMultiTon() { };
	
	public static synchronized PmsiLineType getInstance(String element) {
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
			if (element.equals("EOF"))
				return new EndOfFile();
			else {
				// Opens the config file
				URL configUrlLocation = new URL(null, configUrl,
						new ClasspathHandler(ClassLoader.getSystemClassLoader()));
				
				// Opens the thansformation
				URL configXslLocation = new URL(null, configXslUrl,
						new ClasspathHandler(ClassLoader.getSystemClassLoader()));
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer(
						new StreamSource(configXslLocation.openStream()));
				// Sets the kind of line we have to parse
				transformer.setParameter("linetype", element);
				
				// Input StreamSource
				StreamSource inp = new StreamSource(configUrlLocation.openStream());
	
				// Jaxb object
				JAXBContext jaxbContext = JAXBContext.newInstance(Linetype.class);
			    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			    
				// Creates a config object from the result
			    JAXBResult jaxbResult = new JAXBResult(jaxbUnmarshaller);
			    transformer.transform(inp, jaxbResult);
			    return new PmsiLineTypeImpl((Linetype) jaxbResult.getResult());
			} 

	}
}
