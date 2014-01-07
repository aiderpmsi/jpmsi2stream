package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.HashMap;
import java.util.Map;

public class LineMultiTon {

	private static final Map<Class<? extends PmsiLineType>, PmsiLineType> instances =
			new HashMap<Class<? extends PmsiLineType>, PmsiLineType>();
	
	private LineMultiTon() {	};
	
	public static synchronized <T extends PmsiLineType> T getInstance(Class<T> clas) {
		// Get the singleton instance
		PmsiLineType instance = instances.get(clas);
        
		if (instance == null)
			try {
				// Creates it if not created
				instance = clas.newInstance();
				
				// Stores it
				instances.put(clas, instance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		
		// casts the class (unavoidable warning from generics)
		@SuppressWarnings("unchecked")
		T newInstance = (T) instance;
		
		return newInstance;
	}

}
