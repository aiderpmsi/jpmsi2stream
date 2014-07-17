package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.aiderpmsi.pims.parser.model.PmsiElementConfig;
import com.github.aiderpmsi.pims.parser.model.LineTypeDefinition;

public class LineConfDictionary {

	private static final String configPath = "com/github/aiderpmsi/pims/parser/linedefs.cfg";
	
	private Map<String, LineTypeDefinition> lines =
			new HashMap<>();

	public synchronized LineTypeDefinition getLineConf(String element) {
		// GETS THE LINETYPE FROM HASHMAP IF EXISTS
		LineTypeDefinition conf;
        
		if ((conf = lines.get(element)) == null) {
			// THE INSTANCE DOESN'T EXIST
			try {
				conf = createLineType(element);
			} catch (IOException | URISyntaxException e) {
				throw new RuntimeException(e);
			}
			
			lines.put(element, conf);
		}
		
		return conf;
	}
	
	private LineTypeDefinition createLineType(String element) throws IOException, URISyntaxException {
		// OPENS THE CONFIG FILE
		try (
				InputStream is = this.getClass().getClassLoader().getResourceAsStream(configPath);
				InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
				BufferedReader reader = new BufferedReader(isr);
				) {
			
			String line;
			String typeLine = "id:" + element;

			while ((line = reader.readLine()) != null) {
				if (line.equals(typeLine)) {
					// WE FOUND THE LINE, STORE THE LINETYPE
					LineTypeDefinition lineConf = new LineTypeDefinition();
						
					// FIND TYPE
					while ((line = reader.readLine()) != null && !line.startsWith("type:")) { }
					// WE ARE ON THE TYPE OR ON EOF, THROW AN EXCEPTION
					if (line == null)
						throw new IllegalArgumentException("Type " + element + " not found in config file");

					// GETS THE NAME OF THE LINE
					lineConf.name = line.substring(5);
						
					// THEN FOR EACH NAME, PROCESS CONTENT
					List<PmsiElementConfig> elts = new ArrayList<>();
					PmsiElementConfig elt = null;
					while ((line = reader.readLine()) != null) {
						if (line.startsWith("name:")) {
							if (elt != null) {
								elts.add(elt);
							}
							elt = new PmsiElementConfig();
							elt.name = line.substring(5);
						} else if (line.startsWith("size:")) {
							elt.size = Integer.parseInt(line.substring(5));
						} else if (line.startsWith("type:")) {
							elt.type = line.substring(5);
						} else if (line.startsWith("id:")) {
							break;
						}
					}
						
					// LAST ELEMENT MUST BE ADDED
					if (elt != null)
						elts.add(elt);
					lineConf.elements = elts;
						
					// SET LINE VERSION
					lineConf.version = element;
						
					// SEND LINETYPE
					return lineConf;
				}
			}
			throw new IOException(element + " not found in config file");
		} 

	}
}
