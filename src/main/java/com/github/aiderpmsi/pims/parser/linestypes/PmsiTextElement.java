package com.github.aiderpmsi.pims.parser.linestypes;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiTextElement extends PmsiElementBase {

	public PmsiTextElement(Element config) {
		super(config);
	}

	@Override
	public boolean validate() {
		return true;
	}

}
