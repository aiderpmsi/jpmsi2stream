package com.github.aiderpmsi.pims.grouper.customtags;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.github.aiderpmsi.pims.grouper.model.BaseSimpleDictionary;
import com.github.aiderpmsi.pims.grouper.model.Domain;
import com.github.aiderpmsi.pims.grouper.model.Resource;
import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class Ignore extends Action {
	
	private static final long serialVersionUID = 6879554289822402659L;

	private String resource, resourcekey, domain, domainkey, pattern;

	public Ignore() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// GETS THE RSS CONTENT
		RssContent rssContent =
				(RssContent) scInstance.getRootContext().get("_rssContent");

		// GETS THE RESOURCE
		Resource resourceEnum = Resource.createResource(resource);
		if (resourceEnum != null) {
			switch (resourceEnum) {
			case UNCLASSIFIED:
			case ACTECLASSANT:
			case CLASSEACTE:
			case ACTECLASSANTOP:
				BaseSimpleDictionary dico =
				(BaseSimpleDictionary) scInstance.getRootContext().get("_" + resourceEnum.getName() + "_dictionary");
				// GETS THE DOMAIN
				Domain domainEnum = Domain.createResource(domain);
				if (domainEnum != null) {
					switch(domainEnum) {
					case ACTE:
						remove(dico, resourcekey, rssContent.getRssacte(), domainkey, pattern);
						break;
					case DA:
						remove(dico, resourcekey, rssContent.getRssda(), domainkey, pattern);
						break;
					case DAD:
						remove(dico, resourcekey, rssContent.getRssdad(), domainkey, pattern);
						break;
					default:
						throw new ModelException("domain " + domain + " is not usable in ignore");
					}
				}
				break;
			default:
				throw new ModelException(resource + " is not a possible resource for " + getClass().toString());
			}
		} else {
			throw new ModelException("Resource " + resource + " does not exist");
		}
	}

	private void remove(BaseSimpleDictionary dico, String resourcekey,
			List<HashMap<String, String>> content, String domainkey, String pattern) throws ModelException {
		// GETS THE DEFINITION IN DICO
		Set<String> dicoContent = dico.getDefintion(resourcekey);

		// MESSAGE FORMATER TO WRITE ACCORDING TO THE DESIRED PATTERN
		MessageFormat mf = new MessageFormat(pattern);
		// GETS THE ELEMENTS IN DOMAINKEY, REFORMAT IT WITH PATTERN AND COMPARE IT WITH THE DEFINITIONS
		Iterator<HashMap<String, String>> it = content.iterator();
		extractvalues : while (it.hasNext()) {
			HashMap<String, String> map = it.next();
			// GETS THE DOMAINKEYS
			String[] domainkeys = domainkey.split("\\s*,\\s*");
			// GETS THE DOMAIN VALUES
			String[] domainvalues = new String[domainkeys.length];
			for (int i = 0 ; i < domainkeys.length ; i++) {
				domainvalues[i] = map.get(domainkeys[i]);
				// IF VALUE IS NULL, CHECK NEXT ELEMENT IN CONTENT
				if (domainvalues[i] == null)
					break extractvalues;
			}
			// REFORMAT THE VALUES
			String formattedValue = mf.format(domainvalues);
			// CHECK IF THIS FORMATTED VALUE EXISTS IN DICOCONTENT
			if (dicoContent.contains(formattedValue)) {
				// REMOVE THIS ELEMENT FROM CONTENT IF EXISTS IN DICOCONTENT
				it.remove();
			} // ELSE DO NOTHING
		}
	}
	

}
