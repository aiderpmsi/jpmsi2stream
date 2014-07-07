package com.github.aiderpmsi.pims.parser.linestypes.elements;

import com.github.aiderpmsi.pims.parser.linestypes.Segment;
import com.github.aiderpmsi.pims.parser.model.PmsiElementConfig;

public class PmsiFixedElement extends PmsiElementBase {

	private Segment matcher;
	
	public PmsiFixedElement(PmsiElementConfig config) {
		super(config);
		char[] array = new char[size];
		config.type.getChars(6, 6 + size, array, 0);
		matcher = new Segment(array, 0, array.length);
	}

	@Override
	public boolean validate() {
		
		return content.equals(matcher);

	}

}
