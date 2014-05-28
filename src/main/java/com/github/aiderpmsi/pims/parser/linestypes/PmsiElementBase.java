package com.github.aiderpmsi.pims.parser.linestypes;

import javax.swing.text.Segment;

import com.github.aiderpmsi.pims.parser.model.Element;

public abstract class PmsiElementBase implements PmsiElement {

	protected int size;
	
	protected String name;
	
	protected Segment content;
	
	public PmsiElementBase(Element config) {
		size = config.size;
		name = config.name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Segment getContent() {
		return content;
	}

	@Override
	public int getSize() {
		return size;
	}
	
}
