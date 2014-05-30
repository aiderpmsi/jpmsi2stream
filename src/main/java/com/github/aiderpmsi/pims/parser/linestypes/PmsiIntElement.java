package com.github.aiderpmsi.pims.parser.linestypes;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiIntElement extends PmsiElementBase {

	public PmsiIntElement(Element config) {
		super(config);
	}

	@Override
	public boolean validate() {

		// 0. INCREMENTS POSITION IN ARRAY
		int position = content.offset;
		// 1. IGNORE FIRST SPACES
		int lastsegtelement = content.offset + content.count;
		for (; position < lastsegtelement ; position++) {
			if (content.array[position] != ' ')
				break;
		}
		
		// 2. IGNORES FIRST NUMBERS AND CHECKS IF THERE IS AN INT
		for (; position < lastsegtelement ; position++) {
			if (!Character.isDigit(content.array[position]))
				break;
		}
		
		// 3. IGNORES SUCCEDDING SPACES
		for (; position < lastsegtelement ; position++) {
			if (content.array[position] != ' ')
				break;
		}

		// 4. VERIFY THAT WE ARE AT THE END OF THE SEGMENT
		if (position == lastsegtelement)
			return true;
		else
			return false;
	}

}
