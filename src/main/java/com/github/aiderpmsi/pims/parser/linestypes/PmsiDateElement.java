package com.github.aiderpmsi.pims.parser.linestypes;

import javax.swing.text.Segment;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiDateElement extends PmsiElementBase {
	
	private final static int[] positions = {8, 9, 5, 6, 0, 1, 2, 3};
	
	public PmsiDateElement(Element config) {
		super(config);
	}

	@Override
	public boolean parse(Segment segt) {
		char[] date = new char[10]; 
		this.content = new Segment(date, 0, 10);
		
		// 0. POSITION IN ARRAY
		int position = segt.offset;
		int lastsegtelement = segt.offset + segt.count;
		
		// 1. CHECK THAT ALL ELEMENTS ARE NUMERIC
		for (; position <  lastsegtelement ; position++) {
			if (!Character.isDigit(segt.array[position]))
				return false;
		}
		
		// 2. CREATES THE ISO DATE
		position = segt.offset;
		date[7] = date[4] = '-';
		for (int i : positions) {
			date[i] = segt.array[position++];
		}
		
		return true;
	}

}
