package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.aiderpmsi.pims.parser.model.Element;
import com.github.aiderpmsi.pims.parser.model.Linetype;

public class LineDictionary {

	private static final String configPath = "com/github/aiderpmsi/pims/parser/linedefs.cfg";
	
	private Map<String, PmsiLineType> lines =
			new HashMap<String, PmsiLineType>();

	public synchronized PmsiLineType getLine(String element) {
		// GETS THE LINETYPE FROM HASHMAP IF EXISTS
		PmsiLineType instance;
        
		if ((instance = lines.get(element)) == null) {
			// THE INSTANCE DOESN'T
			try {
				instance = createLine(element);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			lines.put(element, instance);
		}
		
		return instance;
	}
	
	private PmsiLineType createLine(String element) throws IOException {
			// IF EOF IS ASKED
			if (element.equals("eof"))
				return new EndOfFile();
			else {
				// OPENS THE CONFIG FILE
				BufferedReader config = 
						new BufferedReader(new InputStreamReader(LineDictionary.class.getClassLoader().getResourceAsStream(configPath), "UTF-8"));
				
				String line;
				String typeLine = "id:" + element;
				while ((line = config.readLine()) != null) {
					if (line.equals(typeLine)) {
						// WE FOUND THE LINE, STORE THE LINETYPE
						Linetype lineConf = new Linetype();
						
						// FIND TYPE
						while ((line = config.readLine()) != null && !line.startsWith("type:")) { }
						// WE ARE ON THE TYPE OR ON EOF
						if (line == null) throw new IOException("Config file malformed");
						lineConf.setName(line.substring(5));
						
						// THEN FOR EACH NAME, PROCESS CONTENT
						List<Element> elts = new ArrayList<>();
						Element elt = null;
						while ((line = config.readLine()) != null) {
							if (line.startsWith("name:")) {
								if (elt != null) {
									elts.add(elt);
								}
								elt = new Element();
								elt.setIn("");
								elt.setOut("");
								elt.setName(line.substring(5));
							} else if (line.startsWith("pattern:")) {
								elt.setPattern(line.substring(8));
							} else if (line.startsWith("in:")) {
								elt.setIn(line.substring(3));
							} else if (line.startsWith("out:")) {
								elt.setOut(line.substring(4));
							} else if (line.startsWith("id:")) {
								break;
							}
						}
						// LAST ELEMENT CAN BE FORGET WHEN IT WAS THE LAST ELEMENT
						if (elt != null)
							elts.add(elt);
						lineConf.setElements(elts);
						
						// SEND LINETYPE
						return new PmsiLineTypeImpl(lineConf);
					}
				}
				throw new IOException(element + " not found in config file");
			} 

	}
}
