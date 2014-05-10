package com.github.aiderpmsi.pims.grouper.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class BaseSimpleDictionary implements Dictionary<HashSet<String>> {

	public abstract String getConfigPath();
	
	protected Map<String, HashSet<String>> dictionnary =
			new HashMap<>();
	
	public HashSet<String> getDefintion(String key) {
		HashSet<String> definition = dictionnary.get(key);
        
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
	
	protected HashSet<String> createDefinition(String key) throws IOException {
		// OPENS THE CONFIG FILE
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(
							new BufferedInputStream(
									BaseSimpleDictionary.class.getClassLoader().getResourceAsStream(getConfigPath())), "UTF-8"));
			
			HashSet<String> def = new HashSet<>();
			String line = br.readLine();
			
			while (line != null) {
				// CHECK IF WE HAVE THE KEY
				if (line.startsWith("01:") && line.subSequence(3, line.length()).equals(key)) {
					// ITERATE OVER FILE WHILE WE HAVE A VALUE
					while ((line = br.readLine()) != null && line.startsWith("02:")) {
						def.add(line.substring(3, line.length()));
					}
					// WE FINISHED, EXIT IMMEDIATELY
					break;
				} else {
					line = br.readLine();
				}
			}
			return def;
			
		} finally {
			if (br != null)
				br.close();
		}
		
	}
}
