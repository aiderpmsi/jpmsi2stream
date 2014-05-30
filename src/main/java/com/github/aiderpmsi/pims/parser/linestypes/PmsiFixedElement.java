package com.github.aiderpmsi.pims.parser.linestypes;

import javax.swing.text.Segment;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiFixedElement extends PmsiElementBase {

	private Segment matcher;
	
	public PmsiFixedElement(Element config) {
		super(config);
		char[] array = new char[size];
		config.type.getChars(6, 6 + size, array, 0);
		this.matcher = new Segment(array, 0, size);
	}

	@Override
	public boolean validate() {
		
		if (content.count != matcher.count)
			return false;
		
		for (int i = 0 ; i < size ; i++) {
			if (matcher.array[matcher.offset + i] != content.array[content.offset + i])
				return false;
		}

		return true;

	}

}
