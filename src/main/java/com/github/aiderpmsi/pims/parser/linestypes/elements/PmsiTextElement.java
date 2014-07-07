package com.github.aiderpmsi.pims.parser.linestypes.elements;

import com.github.aiderpmsi.pims.parser.model.PmsiElementConfig;

public class PmsiTextElement extends PmsiElementBase {

	public PmsiTextElement(PmsiElementConfig config) {
		super(config);
	}

	@Override
	public boolean validate() {
		return true;
	}

}
