package com.github.aiderpmsi.pims.parser.linestypes;

import javax.swing.text.Segment;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiTextElement extends PmsiElementBase {

	public PmsiTextElement(Element config) {
		super(config);
	}

	@Override
	public boolean parse(Segment segt) {
		this.content = segt;
		return true;
	}

}
