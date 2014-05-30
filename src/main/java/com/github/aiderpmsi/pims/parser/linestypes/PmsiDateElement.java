package com.github.aiderpmsi.pims.parser.linestypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiDateElement extends PmsiElementBase {
	
	public PmsiDateElement(Element config) {
		super(config);
	}

	@Override
	public boolean validate() {
		if (content == null)
			return false;
		
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
		format.setCalendar(new GregorianCalendar());
        format.setLenient(false);
        
        try {
        	format.parse(content.toString());
        } catch (ParseException | IllegalArgumentException e) {
            return false;
        }
        return true;

	}

}
