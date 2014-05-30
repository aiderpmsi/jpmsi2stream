package com.github.aiderpmsi.pims.parser.linestypes;

import javax.swing.text.Segment;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiFixedElement implements PmsiElement {

	protected int size;
	
	protected String name;
	
	protected Segment matcher;
	
	protected String version;
	
	public PmsiFixedElement(Element config) {
		size = config.size;
		name = config.name;
		version = config.version;
		char[] array = new char[size];
		config.type.getChars(6, 6 + size, array, 0);
		this.matcher = new Segment(array, 0, size);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Segment getContent() {
		return matcher;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean parse(Segment segt) {
		if (segt.count != matcher.count)
			return false;
		for (int i = 0 ; i < size ; i++) {
			if (matcher.array[matcher.offset + i] != segt.array[segt.offset + i])
				return false;
		}
		return true;

	}

	@Override
	public String getVersion() {
		return version;
	}

}
