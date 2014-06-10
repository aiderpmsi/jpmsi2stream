package com.github.aiderpmsi.pims.grouper.model;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RssContent {
	
	private EnumMap<RssMain, String> rssmain;
	private List<EnumMap<RssActe, String>> rssacte = new ArrayList<>();
	private List<EnumMap<RssDa, String>> rssda = new ArrayList<>();
	
	private Pattern diag = Pattern.compile("^([A-Z]\\d{2})([^ ]*)\\s*");
	private Pattern calendar = Pattern.compile("^(\\d{2})(\\d{2})(\\d{4})");

	public Object get(String pattern, String type, Enum<?>... defs) throws IOException {
		// VERIFY THAT EVERY DEFINITIONS COMES FROM THE SAME ENUM
		Class<? extends Enum<?>> origin = null;
		for (Enum<?> def : defs) {
			if (origin == null) {
				origin = def.getDeclaringClass();
			} else if (origin != def.getDeclaringClass()) {
				throw new IOException("Class " + def.getDeclaringClass() + " is not of " + origin + " type");
			}
		}
		
		if (origin == RssMain.class) {
			return getValue(rssmain, defs, pattern, type);
		} else if (origin == RssActe.class) {
			return getValues(rssacte, defs, pattern, type);
		} else if (origin == RssDa.class) {
			return getValues(rssda, defs, pattern, type);
		} else {
			throw new IOException(origin + " is not a known resource enum is rss");
		}
	}

	private Object getValue(EnumMap<? extends Enum<?>, String> content, Enum<?>[] defs, String pattern, String type) throws IOException {
		// USE A SPECIAL CASE OF GET VALUES
		List<?> values = getValues(Arrays.asList(content), defs, pattern, type);
		// IF NO VALUE EXIST, RETURN NULL, ELSE THE FIRST ELEMENT
		if (values.size() == 0)
			return null;
		else
			return values.get(0);
	}

	private List<?> getValues(List<?> content, Enum<?>[] defs, String pattern, String type) throws IOException {		
		// LIST OF DEMANDED ELEMENT
		List<String[]> valueslist = new ArrayList<>(content.size());
	
		// FOR EACH ELEMENT IN DA, OR ACTE, EXTRACT THE NECESSARY ELEMENTS
		extractvalues : for (Object element : content) {
			@SuppressWarnings("unchecked")
			EnumMap<? extends Enum<?>, String> castedElement = (EnumMap<? extends Enum<?>, String>) element;
			String[] values = new String[defs.length];
			for (int i = 0 ; i < defs.length ; i++) {
				// CONSIDER THE CASE WHERE THE KEY DOESN'T EXIST OR VALUE IS NULL
				String value = castedElement.get(defs[i]);
				if (value == null)
					// IF NO VALUE EXISTS FOR THIS KEY, WE CAN'T CREATE THE VALUE IN VALUELIST
					continue extractvalues;
				else
					values[i] = value.trim(); 
			}
			valueslist.add(values);
		}
	
		// REFORMAT THESE VALUES
		MessageFormat mf = new MessageFormat(pattern);
		List<String> formattedResults = new ArrayList<>(valueslist.size());
		for (String[] values : valueslist) {
			formattedResults.add(mf.format(values));
		}

		// TRANSFORMS THE RESULTS TO THE DESIRED TYPE
		List<Object> castedResults;
		switch(type) {
		case "String":
			return formattedResults;
		case "Integer":
			castedResults = new ArrayList<>(formattedResults.size());
			for (String formattedResult : formattedResults) {
				castedResults.add(formatInteger(formattedResult));
			}
			return castedResults;
		case "Diagnostic":
			castedResults = new ArrayList<>(formattedResults.size());
			for (String formattedResult : formattedResults) {
				castedResults.add(formatDiagnostic(formattedResult));
			}
			return castedResults;
		case "Calendar":
			castedResults = new ArrayList<>(formattedResults.size());
			for (String formattedResult : formattedResults) {
				castedResults.add(formatCalendar(formattedResult));
			}
			return castedResults;
		default:
			throw new IOException(type + " is not a possible type");
		}
	}
	
	public Integer formatInteger(String value) throws IOException {
		try {
			return new Integer(value);
		} catch (NumberFormatException e) {
			throw new IOException(e);
		}
	}

	public String formatDiagnostic(String value) throws IOException {
		Matcher m;
		if (value.length() == 0) {
			// THIS IS A VOID DIAGNOSIS, RETURN IT
			return value;
		} else if ((m = diag.matcher(value)).matches()) {
			// WE HAVE A FULL DIAGNOSIS
			StringBuilder acte = new StringBuilder(m.group(1));
			if (m.group(2).length() != 0)
				acte.append(".").append(m.group(2));
			return acte.toString();
		} else {
			throw new IOException(value + " is not a diagnosis");
		}
	}

	public Calendar formatCalendar(String value) throws IOException {
		Matcher m = calendar.matcher(value);
		if (m.matches()) {
			Calendar cal = new GregorianCalendar(
					new Integer(m.group(3)),
					new Integer(m.group(2))- 1 ,
					new Integer(m.group(1)));
			return cal;
		} else {
			throw new IOException(value + " is not a calendar date");
		}
	}
	
	public void setRssmain(EnumMap<RssMain, String> rssmain) {
		this.rssmain = rssmain;
	}
	
	public void setRssacte(List<EnumMap<RssActe, String>> rssacte) {
		this.rssacte = rssacte;
	}
	
	public void setRssda(List<EnumMap<RssDa, String>> rssda) {
		this.rssda = rssda;
	}

	public EnumMap<RssMain, String> getRssmain() {
		return rssmain;
	}

	public List<EnumMap<RssActe, String>> getRssacte() {
		return rssacte;
	}

	public List<EnumMap<RssDa, String>> getRssda() {
		return rssda;
	}
	
}