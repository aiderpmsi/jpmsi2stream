package com.github.aiderpmsi.pims.grouper.model;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Utils {
	
	private Dictionaries dicos;
	
	public Utils(Dictionaries dicos) {
		this.dicos = dicos;
	}
	
	public Integer count(String resource, String key, Object value) throws IOException {
		// GETS THE DICO
		SimpleDictionary dico = dicos.get(resource);
		// GETS THE DEFINITION IN DICO
		Set<String> dicoContent = dico.getDefintion(key);

		// RESULT
		Integer matches = 0;
		
		// IF VALUE IS NULL, WE RETURN FALSE
		if (value == null) {
			// DO NOTHING, MATCHES IS ALREADY FALSE
		}
		// IF WE HAVE TO CHECK AGAINST A COLLECTION, CHECK EACH ITEM
		else if (value instanceof Collection<?>) {
			for (Object element : (Collection<?>) value) {
				if (element instanceof String) {
					if (dicoContent.contains(((String) element).trim())) {
						matches++;
					}
				}
			}
		}
		// IF WE HAVE TO CHECK AGAINST A STRING, CHECK THIS STRING
		else if (value instanceof String) {
			if (dicoContent.contains(((String) value).trim())) {
				matches++;
			}
		}
		
		return matches;

	}

	@SuppressWarnings("unchecked")
	public HashSet<String> concat(Object... values) throws IOException {
		
		// SETS THE RESULT HASH
		HashSet<String> resultHash = new HashSet<>();
		
		for (Object value : values) {
			
			// VALUE IS NULL
			if (value == null) {
				// DO NOTHING
			}
			
			// VALUE IS A STRING
			else if (value instanceof String) {
				resultHash.add((String) value);
			}
			
			// VALUE IS A COLLECTION
			else if (value instanceof Collection<?>) {
				for (Object valueObject : (Collection<Object>) value) {
					if (valueObject instanceof String) {
						resultHash.add((String) valueObject);
					}
				}
			}
			else {
				throw new IOException(value + " is not a known type for Concatenate");
			}
		}
		return resultHash;
	}

	public Integer duration(Calendar begining, Calendar end, String type) throws IOException {

		// CALCULATE AGE
		Integer age;
		switch (type) {
		case "year":
			age = end.get(Calendar.YEAR) - begining.get(Calendar.YEAR);
			if (end.get(Calendar.MONTH) < begining.get(Calendar.MONTH))
				age--;
			else if (end.get(Calendar.MONTH) == begining.get(Calendar.MONTH) &&
					end.get(Calendar.DAY_OF_MONTH) < begining.get(Calendar.DAY_OF_MONTH))
				age--;
			break;
		case "day":
			age = new Long((end.getTimeInMillis() - begining.getTimeInMillis()) / 86400000L).intValue();
			break;
		default:
			throw new IOException(type + " is not an accepted type");
		}

		return age;
	}
	
	public void awrdp(List<String> actes) throws IOException {
		HashSet<String> dicoacteschirmineur = dicos.get("actemineurchirreclassant").getDefintion("all");
		HashSet<String> dicoacteschir = dicos.get("classeacte").getDefintion("ADC");

		// LIST OF ACTES CHIR MINEUR
		HashSet<String> acteschirmineur = new HashSet<>();
		for (String acte : actes) {
			if (dicoacteschirmineur.contains(acte))
				acteschirmineur.add(acte);
		}
		// LIST OF ACTES CHIR
		HashSet<String> acteschir = new HashSet<>();
		for (String acte : actes) {
			if (dicoacteschir.contains(acte))
				acteschir.add(acte);
		}
		
		if (acteschir.equals(acteschirmineur)) {
			// IF EVERY ACTE CHIR BELONGS TO ACTES CHIR MINEUR LIST, IGNORE THEM
			remove(actes, acteschir);
		} else {
			// ELSE IF THERE IS AT LEAST ONE ACTE CLASSANT OP, IGNORE IT
			HashSet<String> dicoactesclassantsop = dicos.get("acteclassantop").getDefintion("all");
			remove(actes, dicoactesclassantsop);
		}
	}
	
	private void remove(List<String> actes, HashSet<String> toRemove) {
		// REMOVE CORRESPONDING ACTES
		Iterator<String> it = actes.iterator();
		while (it.hasNext()) {
			String acte = it.next();
			// CHECK IF THIS FORMATTED VALUE EXISTS IN DICOCONTENT
			if (toRemove.contains(acte)) {
				// REMOVE THIS ELEMENT FROM CONTENT IF EXISTS IN DICOCONTENT
				it.remove();
			} // ELSE DO NOTHING
		}
	}

}
