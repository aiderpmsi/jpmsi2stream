package com.github.aiderpmsi.pims.grouper.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;

public class SimpleDictionary {
	
	/**
	 * Enum listing all the grouper configuration files 
	 * @author jpc
	 *
	 */
	public enum Type {
		acteClassant("grouper-acteclassant.cfg"),
		acteClassantOp("grouper-acteclassantop.cfg"),
		acteMineurChirReclassant("grouper-actemineurchirreclassant.cfg"),
		acteNonOpCourt("grouper-actenonopcourt.cfg"),
		acteNonOpTherap("grouper-actenonoptherap.cfg"),
		autreActeClasssantNonOp("grouper-autreacteclassantnonop.cfg"),
		classeActe("grouper-classeacte.cfg"),
		cmaExcDp("grouper-cma-exc-dp.cfg"),
		cmaExcGh("grouper-cma-exc-gh.cfg"),
		cma("grouper-cma.cfg"),
		diagImprecis("grouper-diagimprecis.cfg"),
		dpClassant("grouper-dpclassant.cfg"),
		unclassified("grouper-unclassified.cfg");
			
		public String name;
			
		private Type(String name) {
			this.name = name;
		}
	}
	
	/** Type of this dictionary (defines the config file used too) */
	private Type type;
	
	/** Cache for this dictionary */
	private HashMap<String, HashSet<String>> content = new HashMap<>();

	/**
	 * Constructor. Needs the kind of dictionary (for the config file definition)
	 * @param type
	 */
	public SimpleDictionary(Type type) {
		this.type = type;
	}

	/**
	 * Gets the list of elements contained for a key
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	public HashSet<String> getDefinition(String key) throws IOException {
		HashSet<String> definition;
	        
		// IF KEY DEFINITION DOES NOT EXIST, CREATE IT AND LOAD IT
		if ((definition = content.get(key)) == null) {
				
			definition = createDefinition(key);
				
			content.put(key, definition);
		}
			
		return definition;
	}
	
	/**
	 * If a list of elements contained for a key is not cached, read it from the config file
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	protected HashSet<String> createDefinition(String key) throws IOException {
		// OPENS THE CONFIG FILE
		try (
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("com/github/aiderpmsi/pims/grouper/" + type.name);
				InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);
				) {

			String line;
		
			// READ UNTIL REACHING THE NEEDED KEY 
			while ((line = br.readLine()) != null) {
				// CHECK IF WE HAVE THE KEY
				if (line.startsWith("01:") && line.subSequence(3, line.length()).equals(key)) {
					// WE HAVE THE KEY, CREATE THE BUFFER
					HashSet<String> def = new HashSet<>();
					// ITERATE OVER FILE WHILE WE HAVE A VALUE
					while ((line = br.readLine()) != null && line.startsWith("02:")) {
						def.add(line.substring(3, line.length()));
					}
					// WE FINISHED, EXIT IMMEDIATELY
					return def;
				}
			}
			
			// IF WE ARRIVE HERE, IT MEANS THE KEY WAS NOT FOUND, THROW IOEXCEPTION
			throw new IOException("Key " + key + " not found in " + type.name);
		}
	}

}
	
