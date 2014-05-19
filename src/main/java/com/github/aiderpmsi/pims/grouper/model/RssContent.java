package com.github.aiderpmsi.pims.grouper.model;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RssContent {
	
	private HashMap<String, String> rssmain;
	private List<HashMap<String, String>> rssacte = new ArrayList<>();
	private List<HashMap<String, String>> rssda = new ArrayList<>();
	private List<HashMap<String, String>> rssdad = new ArrayList<>();

	public Object get(String domain, String key, String pattern, String type) throws IOException {
		// GETS THE DEMANDED KEYS IN RETRIEVE AND TRIM THEM
		String[] keys = key.split(",");
		for (int i = 0 ; i < keys.length ; i++) {
			keys[i] = keys[i].trim();
		}

		switch(domain) {
			case "main":
				return getValue(getRssmain(), keys, pattern, type);
			case "acte":
				return getValues(getRssacte(), keys, pattern, type);
			case "da":
				return getValues(getRssda(), keys, pattern, type);
			case "dad":
				return getValues(getRssdad(), keys, pattern, type);
			default:
				throw new IOException(domain + " is not a known domain is rss");
		}
	}

	private Object getValue(HashMap<String, String> content, String[] keys, String pattern, String type) throws IOException {
		// USE A SPECIAL CASE OF GET VALUES
		List<?> values = getValues(Arrays.asList(content), keys, pattern, type);
		// IF NO VALUE EXIST, RETURN NULL, ELSE THE FIRST ELEMENT
		if (values.size() == 0)
			return null;
		else
			return values.get(0);
	}

	private List<?> getValues(List<HashMap<String, String>> content, String[] keys, String pattern, String type) throws IOException {
		// GETS THE LIST OF DEMANDED ELEMENT
		List<String[]> valueslist = new ArrayList<>(content.size());
	
		// FOR EACH ELEMENT IN DA, DAD OR ACTE, EXTRACT THE NECESSARY ELEMENTS
		extractvalues : for (HashMap<String, String> element : content) {
			String[] values = new String[keys.length];
			for (int i = 0 ; i < keys.length ; i++) {
				// CONSIDER THE CASE WHERE THE KEY DOESN'T EXIST OR VALUE IS NULL
				String value = element.get(keys[i]);
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
		try {
			switch(type) {
			case "String":
				return formattedResults;
			case "Integer":
				List<Integer> castedResults = new ArrayList<>(formattedResults.size());
				for (String formattedResult : formattedResults) {
					castedResults.add(new Integer(formattedResult));
				}
				return castedResults;
			case "Diagnostic":
				Pattern pat = Pattern.compile("^([A-Z]\\d{2})([^ ]*)\\s*");
				List<String> acteResults = new ArrayList<>(formattedResults.size());
				for (String formattedResult : formattedResults) {
					Matcher m = pat.matcher(formattedResult);
					if (m.matches()) {
						StringBuilder acte = new StringBuilder(m.group(1));
						if (m.group(2).length() != 0)
							acte.append(".").append(m.group(2));
						acteResults.add(acte.toString());
					} else {
						throw new IOException(formattedResult + " is not a diagnosis");
					}
				}
				return acteResults;
			case "Calendar":
				Pattern patcal = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})");
				List<Calendar> acteDates = new ArrayList<>(formattedResults.size());
				for (String formattedResult : formattedResults) {
					Matcher m = patcal.matcher(formattedResult);
					if (m.matches()) {
						Calendar cal = new GregorianCalendar(
								new Integer(m.group(1)),
								new Integer(m.group(2))- 1 ,
								new Integer(m.group(3)));
						acteDates.add(cal);
					} else {
						throw new IOException(formattedResult + " is not a calendar date");
					}
				}
				return acteDates;
			default:
				throw new IOException(type + " is not a possible type");
			}
		} catch (NumberFormatException e) {
			throw new IOException(e);
		}
	}

	
	public HashMap<String, String> getRssmain() {
		return rssmain;
	}
	
	public void setRssmain(HashMap<String, String> rssmain) {
		this.rssmain = rssmain;
	}
	
	public List<HashMap<String, String>> getRssacte() {
		return rssacte;
	}
	
	public void setRssacte(List<HashMap<String, String>> rssacte) {
		this.rssacte = rssacte;
	}
	
	public List<HashMap<String, String>> getRssda() {
		return rssda;
	}
	
	public void setRssda(List<HashMap<String, String>> rssda) {
		this.rssda = rssda;
	}
	
	public List<HashMap<String, String>> getRssdad() {
		return rssdad;
	}
	
	public void setRssdad(List<HashMap<String, String>> rssdad) {
		this.rssdad = rssdad;
	}
	
}