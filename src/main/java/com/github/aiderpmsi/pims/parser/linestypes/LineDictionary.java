package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.pims.parser.jaxb.Linetype;

public class LineDictionary {

	private static final String configPath = "com/github/aiderpmsi/pims/parser/linedefs.xml";
	private static final String configXslPath = "com/github/aiderpmsi/pims/parser/linedefsset.xsl";
	
	private Map<String, PmsiLineType> instances =
			new HashMap<String, PmsiLineType>();

	private Transformer transformer = null;
	
	private Document document = null;
	
	private Unmarshaller jaxbUnmarshaller = null;
	
	public LineDictionary() {
		try {
			// CREATES THE TRANSFORMER ONLY ONCE
			// 1. TRANSFORMER XSL
			InputStream configXslStream = 
					LineDictionary.class.getClassLoader().getResourceAsStream(configXslPath);
			// 2. CREATES THE TRANSFORMER
			TransformerFactory tFactory = org.apache.xalan.processor.TransformerFactoryImpl.newInstance();
				transformer = tFactory.newTransformer(
						new StreamSource(configXslStream));
			
			// CREATES THE DOMSOURCE ONLY ONCE
			// 1. DOM BUILDERS
			DocumentBuilderFactory docbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = docbfactory.newDocumentBuilder();
			// 2. CONFIG STREAM
			InputStream configStream = 
					LineDictionary.class.getClassLoader().getResourceAsStream(configPath);
			// 3. CREATES DOM
			document = builder.parse(configStream);
			
			// CREATES UNMARSHMALLER ONCE
			JAXBContext jaxbContext = JAXBContext.newInstance(Linetype.class);
		    jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		} catch (TransformerConfigurationException | ParserConfigurationException | SAXException | IOException | JAXBException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public synchronized PmsiLineType getInstance(String element) {
		// GETS THE LINETYPE FROM HASHMAP IF EXISTS
		PmsiLineType instance;
        
		if ((instance = instances.get(element)) == null) {
			// THE INSTANCE DOESN'T
			try {
				instance = createInstance(element);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			instances.put(element, instance);
		}
		
		return instance;
	}
	
	private PmsiLineType createInstance(String element) throws IOException, JAXBException, TransformerException {
			// IF EOF IS ASKED
			if (element.equals("eof"))
				return new EndOfFile();
			else {
				// SETS THE KIND OF LINE WE HAVE TO PARSE
				transformer.setParameter("linetype", element);
				
				// CONFIG SOURCE
				DOMSource source = new DOMSource(document);
				    			    
				// CREATES THE LINE TYPE
			    JAXBResult jaxbResult = new JAXBResult(jaxbUnmarshaller);
			    transformer.transform(source, jaxbResult);

			    return new PmsiLineTypeImpl((Linetype) jaxbResult.getResult());
			} 

	}
}
