package com.github.aiderpmsi.pims.grouper.customtags;

import java.io.IOException;
import java.util.Calendar;

public class Duration {
	
	public Integer calculate(Calendar begining, Calendar end, String type) throws IOException {

		// CALCULATE AGE
		Integer age;
		switch (type) {
		case "year":
			age = end.get(Calendar.YEAR) - begining.get(Calendar.YEAR);
			if (end.get(Calendar.MONTH) < begining.get(Calendar.MONTH))
				age--;
			else if (end.get(Calendar.MONTH) == begining.get(Calendar.MONTH) &&
					end.get(Calendar.DAY_OF_MONTH) < begining.get(Calendar.DAY_OF_MONTH))
				age--;
			break;
		case "day":
			age = new Long((end.getTimeInMillis() - begining.getTimeInMillis()) / 86400000L).intValue();
			break;
		default:
			throw new IOException(type + " is not an accepted type");
		}

		return age;
	}

}
