package com.github.aiderpmsi.pims.grouper.customtags;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class Concatenate {

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

}
