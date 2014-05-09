package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Calendar;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

public class Age extends Action {

	private static final long serialVersionUID = 4726690992828290473L;

	private String[][] dates = {
		{"birthday", null},
		{"date", null}
	};
	
	private String result, type;
	
	@Override
	@SuppressWarnings({ "rawtypes"})
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		
		// GET THE BIRTHDAY AND DATE TO COMPUTE
		Calendar[] datesCal = new Calendar[2];
		for (int i = 0 ; i < dates.length ; i++) {
			// GETS THE OBJECT IN VARNAME
			Object calObject = scInstance.getContext(getParentTransitionTarget()).get(dates[i][1]);
			// VERIFY IF IT IS NOT NULL
			if (calObject == null)
				throw new ModelException("Value of " + dates[i][0] + " (" + dates[i][1] + ") is null");
			// TRANSFORM TO CALENDAR IF POSSIBLE
			else if (calObject instanceof Calendar) {
				datesCal[i] = (Calendar) calObject;
			}
			// IF IMPOSSIBLE, THROW EXCEPTION
			else {
				throw new ModelException(dates[i][1] + " is not a calendar element");
			}
		}

		// CALCULATE AGE
		Integer age;
		switch (type) {
		case "year":
			age = datesCal[1].get(Calendar.YEAR) - datesCal[0].get(Calendar.YEAR);
			if (datesCal[1].get(Calendar.MONTH) < datesCal[0].get(Calendar.MONTH))
				age--;
			else if (datesCal[1].get(Calendar.MONTH) == datesCal[0].get(Calendar.MONTH) &&
					datesCal[1].get(Calendar.DAY_OF_MONTH) < datesCal[0].get(Calendar.DAY_OF_MONTH))
				age--;
			break;
		case "day":
			age = new Long((datesCal[1].getTimeInMillis() - datesCal[0].getTimeInMillis()) / 86400000L).intValue();
			break;
		default:
			throw new ModelException(type + " is not an accepted type");
		}
		
		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, age);
	}

	public String getBirthday() {
		return this.dates[0][1];
	}

	public void setBirthday(String birthday) {
		this.dates[0][1] = birthday;
	}

	public String getDate() {
		return this.dates[1][1];
	}

	public void setDate(String date) {
		this.dates[1][1] = date;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
