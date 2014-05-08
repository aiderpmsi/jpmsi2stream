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

	private String birthday, date, result;
	
	@Override
	@SuppressWarnings({ "rawtypes"})
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		
		// GET THE BIRTHDAY AND DATE TO COMPUTE
		Calendar birthdayCal;
		Object birthdayObject = scInstance.getContext(getParentTransitionTarget()).get(birthday);
		if (birthdayObject instanceof Calendar) {
			birthdayCal = (Calendar) birthdayObject;
		} else {
			throw new ModelException(birthday + " is not a calendar element");
		}
		Calendar dateCal;
		Object dateObject = scInstance.getContext(getParentTransitionTarget()).get(date);
		if (dateObject instanceof Calendar) {
			dateCal = (Calendar) dateObject;
		} else {
			throw new ModelException(date + " is not a calendar element");
		}
		
		Integer age = dateCal.get(Calendar.YEAR) - birthdayCal.get(Calendar.YEAR);
		if (dateCal.get(Calendar.MONTH) < birthdayCal.get(Calendar.MONTH))
			age--;
		else if (dateCal.get(Calendar.MONTH) == birthdayCal.get(Calendar.MONTH) &&
				dateCal.get(Calendar.DAY_OF_MONTH) < birthdayCal.get(Calendar.DAY_OF_MONTH))
			age--;
		
		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, age);
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
