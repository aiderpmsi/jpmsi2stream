package com.github.aiderpmsi.pims.grouper.customtags;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class FromRss extends Action {
	
	private enum Domain {

		MAIN("main"), ACTE("acte"), DA("da"), DAD("dad");
			
		private static HashMap<String, Domain> domainsMap = new HashMap<>();
			
		static {
			for (Domain value : Domain.values()) {
				domainsMap.put(value.getName(), value);
			}
		}
			
		private String name;

		private Domain(String name) {
			this.name = name;
		}
			
		public String getName() {
			return name;
		}
			
		public static Domain createResource(String domainName) {
			return domainsMap.get(domainName);
		}
			
	}

	private static final long serialVersionUID = -7371526402853556556L;

	private String domain, get, pattern, result, type;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// Gets the rss content
		RssContent rssContent =
				(RssContent) scInstance.getRootContext().get("_rssContent");

		// GETS THE DEMANDED KEYS IN RETRIEVE AND TRIM THEM
		String[] keys = get.split(",");
		for (int i = 0 ; i < keys.length ; i++) {
			keys[i] = keys[i].trim();
		}

		// RETURNED OBJECT
		Object returnedObject = null;
		
		Domain domainEnum = Domain.createResource(domain);
		if (domainEnum != null) {
			switch(domainEnum) {
			case MAIN:
				returnedObject = getValue(rssContent.getRssmain(), keys, pattern);
				break;
			case ACTE:
				returnedObject = getValues(rssContent.getRssacte(), keys, pattern);
				break;
			case DA:
				returnedObject = getValues(rssContent.getRssda(), keys, pattern);
				break;
			case DAD:
				returnedObject = getValues(rssContent.getRssdad(), keys, pattern);
				break;
			}
		} else {
			throw new ModelException(domain + " is not known");
		}

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, returnedObject);
	}

	private Object getValue(HashMap<String, String> content, String[] keys, String pattern) throws ModelException {
		// USE A SPECIAL CASE OF GET VALUES
		return getValues(Arrays.asList(content), keys, pattern).get(0);
	}

	private List<?> getValues(List<HashMap<String, String>> content, String[] keys, String pattern) throws ModelException {
		// GETS THE LIST OF DEMANDED ELEMENT
		List<String[]> valueslist = new ArrayList<>(content.size());
		
		// FOR EACH ELEMENT IN DA, DAD OR ACTE, EXTRACT THE NECESSARY ELEMENTS
		for (HashMap<String, String> element : content) {
			String[] values = new String[keys.length];
			for (int i = 0 ; i < keys.length ; i++) {
				// CONSIDER THE CASE WHERE THE KEY DOESN'T EXIST OR VALUE IS NULL
				String value = element.get(keys[i]);
				values[i] = (value == null ? "" : value.trim()); 
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
						acteResults.add(m.group(1) + "." + m.group(2));
					} else {
						throw new ModelException(formattedResult + " is not a diagnosis");
					}
				}
				return acteResults;
			default:
				throw new ModelException(type + " is not a possible type");
			}
		} catch (NumberFormatException e) {
			throw new ModelException(e);
		}
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGet() {
		return get;
	}

	public void setGet(String get) {
		this.get = get;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
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
