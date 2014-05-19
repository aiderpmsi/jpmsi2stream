package com.github.aiderpmsi.pims.grouper.customtags;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.SimpleDictionary;

public class InResource {
	
	private Dictionaries dicos;
	
	public InResource(Dictionaries dicos) {
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

}
