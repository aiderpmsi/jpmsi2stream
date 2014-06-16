package com.github.aiderpmsi.pims.grouper.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

public class SimpleDictionary {
	
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
	
	private Type type;
	
	private HashMap<String, HashSet<String>> content = new HashMap<>();

	public SimpleDictionary(Type type) {
		this.type = type;
	}

	public HashSet<String> getDefinition(String key) {
		HashSet<String> definition;
	        
		// IF KEY DEFINITION DOES NOT EXIST, CREATE IT AND LOAD IT
		if ((definition = content.get(key)) == null) {
				
			definition = createDefinition(key);
				
			content.put(key, definition);
		}
			
		return definition;
	}
		
	protected HashSet<String> createDefinition(String key) {
		// OPENS THE CONFIG FILE
		Path origin;
		try {
			origin = Paths.get(getClass().getClassLoader().getResource("com/github/aiderpmsi/pims/grouper/" + type.name).toURI());

			try (BufferedReader br = Files.newBufferedReader(origin, Charset.forName("UTF-8"))) {

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
			}
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}

}
	
