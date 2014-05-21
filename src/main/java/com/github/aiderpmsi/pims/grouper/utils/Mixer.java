package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.Utils;

/**
 * Groups multiple rss
 * @author jpc
 *
 */
public class Mixer {
	
	private Dictionaries dicos = null;

	public RssContent mix(List<RssContent> multirss) throws IOException {
		// GETS THE PRINCIPAL RUM
		Integer rumnb = getPrincipalRumIndex(multirss);
		
		// MIX MULTIRSS WITH RUMNB AS FIRST RUM
		return mix(multirss, rumnb);
	}
	
	private RssContent mix(List<RssContent> multirss, Integer rumnb) throws IOException {
		RssContent rss = new RssContent();
		// SETS THE PRINCIPAL RUM
		rss.setRssmain(multirss.get(rumnb).getRssmain());

		// GETS DP AND DR OF THIS NEW RSS
		String dp = (String) rss.get("main", "DP", "{0}", "Diagnostic");
		String dr = (String) rss.get("main", "DR", "{0}", "Diagnostic");
		
		// SETS THE ACTS AS SUM OF ACTS
		for (RssContent content : multirss) {
			for (HashMap<String, String> acte : content.getRssacte()) {
				rss.getRssacte().add(acte);
			}
		}

		// ADDS EACH DAS AS SUM OF DIAGS (EXCEPT IF IT IS DP OR DR)
		for (RssContent content : multirss) {
			for (HashMap<String, String> diag : content.getRssda()) {
				String diagcim = content.formatDiagnostic(diag.get("DA"));
				if (!diagcim.equals(dp) && !diagcim.equals(dr)) {
					rss.getRssda().add(diag);
				}
			}
		}
		
		// RETURN THIS RSS (IGNORE DAD)
		return rss;
	}
	
	private int getPrincipalRumIndex(Collection<RssContent> multirss) throws IOException {
		Iterator<RssContent> it;
		
		// GETS THE LIST OF ACTECLASSANTOP
		HashSet<String> acteclassantops = dicos.get("acteclassantop").getDefintion("all");
		
		// 1. FIRST RUM WITH LIST 1 ACT IS RETAINED
		it = multirss.iterator();
		for (int i = 0 ; i < multirss.size() ; i++) {
			RssContent cont = it.next();
			String dp = (String) cont.get("main", "DP", "{0}", "Diagnostic");
			if (acteclassantops.contains(dp))
				return i;
		}
		
		// 2. GETS THE RUM WITH ONE DP Z51.5, Z50.2 OR Z50.3 WHICH HAS THE LONGEST STAYING
		Integer stay = null;
		Integer rumnb = null;
		it = multirss.iterator();
		for (int i = 0 ; i < multirss.size() ; i++) {
			RssContent cont = it.next();
			String dp = (String) cont.get("main", "DP", "{0}", "Diagnostic");
			if (dp.equals("Z51.5") || dp.equals("Z50.2") || dp.equals("Z50.3")) {
				Calendar dateentree = (Calendar) cont.get("main", "DateEntree", "{0}", "Calendar");
				Calendar datesortie = (Calendar) cont.get("main", "DateSortie", "{0}", "Calendar");
				Integer dureesejour = (new Utils(dicos)).duration(dateentree, datesortie, "day");
				if (stay == null || stay < dureesejour) {
					stay = dureesejour;
					rumnb = i;
				}
			}
		}
		if (stay != null) {
			return rumnb;
		}
		
		// GETS THE LIST OF ACTENONOPTHERAP
		HashSet<String> actenonoptheraps = dicos.get("actenonoptherap").getDefintion("all");
		
		// 3. THE FIRST RUM WITH ONE ACTENONOPTHERAP IS RETAINED
		it = multirss.iterator();
		for (int i = 0 ; i < multirss.size() ; i++) {
			RssContent cont = it.next();
			for (HashMap<String, String> acte : cont.getRssacte()) {
				String formattedActe = acte.get("CodeCCAM").trim() + "/" + acte.get("Phase").trim();
				if (actenonoptheraps.contains(formattedActe))
					return i;
			}
		}
	
		// GETS THE LIST OF ACTENONOPCOURT
		HashSet<String> actenonopcourts = dicos.get("actenonopcourt").getDefintion("all");

		// 4. THE FIRST RUM WITH ONE ACTENONOPCOURT IS RETAINED (IF STAY DURATION IS LESS OR EQUAL THAN 1 DAY)
		it = multirss.iterator();
		for (int i = 0 ; i < multirss.size() ; i++) {
			RssContent cont = it.next();
			for (HashMap<String, String> acte : cont.getRssacte()) {
				String formattedActe = acte.get("CodeCCAM").trim() + "/" + acte.get("Phase").trim();
				if (actenonopcourts.contains(formattedActe)) {
					Calendar dateentree = (Calendar) cont.get("main", "DateEntree", "{0}", "Calendar");
					Calendar datesortie = (Calendar) cont.get("main", "DateSortie", "{0}", "Calendar");
					Integer dureesejour = (new Utils(dicos)).duration(dateentree, datesortie, "day");
					if (dureesejour <= 1)
						return i;
				}
			}
		}

		// GETS THE LIST OF AUTREACTECLASSANTNONOP
		HashSet<String> autreacteclassantnonops = dicos.get("autreacteclassantnonop").getDefintion("all");
		
		// 5. THE FIRST RUM WITH AUTREACTECLASSANTNONOP AND DURATION IS 0 IS RETAINED
		it = multirss.iterator();
		for (int i = 0 ; i < multirss.size() ; i++) {
			RssContent cont = it.next();
			for (HashMap<String, String> acte : cont.getRssacte()) {
				String formattedActe = acte.get("CodeCCAM").trim() + "/" + acte.get("Phase").trim();
				if (autreacteclassantnonops.contains(formattedActe)) {
					Calendar dateentree = (Calendar) cont.get("main", "DateEntree", "{0}", "Calendar");
					Calendar datesortie = (Calendar) cont.get("main", "DateSortie", "{0}", "Calendar");
					Integer dureesejour = (new Utils(dicos)).duration(dateentree, datesortie, "day");
					if (dureesejour == 0)
						return i;
				}
			}
		}
		
		// 7. IF EVERY RSS HAS A 'S' DIAG, CHOOSE THE ONE WITH THE MAX DURATION
		it = multirss.iterator();
		for (int i = 0 ; i < multirss.size() ; i++) {
			RssContent cont = it.next();
			String dp = (String) cont.get("main", "DP", "{0}", "Diagnostic");
			if (!dp.startsWith("S")) {
				stay = null;
				rumnb = null;
				break;
			} else {
				Calendar dateentree = (Calendar) cont.get("main", "DateEntree", "{0}", "Calendar");
				Calendar datesortie = (Calendar) cont.get("main", "DateSortie", "{0}", "Calendar");
				Integer dureesejour = (new Utils(dicos)).duration(dateentree, datesortie, "day");
				if (stay == null || stay < dureesejour) {
					stay = dureesejour;
					rumnb = i;
				}
			}
		}
		if (stay != null)
			return rumnb;
		
		// GETS THE LIST OF IMPRECISE DIAGS
		HashSet<String> diagsimprecis = dicos.get("diagimprecis").getDefintion("all");

		// 6. IF NOT RUM IS RETAINED, CALCULATE PENALTIES
		Integer[] penalties = new Integer[multirss.size()];
		it = multirss.iterator();
		for (int i = 0 ; i < multirss.size() ; i++) {
			penalties[i] = 0;
			RssContent cont = it.next();
			String dp = (String) cont.get("main", "DP", "{0}", "Diagnostic");
			Calendar dateentree = (Calendar) cont.get("main", "DateEntree", "{0}", "Calendar");
			Calendar datesortie = (Calendar) cont.get("main", "DateSortie", "{0}", "Calendar");
			Integer dureesejour = (new Utils(dicos)).duration(dateentree, datesortie, "day");
			// IF DP NOT Z AND NOT R AND DURATION GREATER OR EQUAL THAN 2, ADD 100 POINTS FOR EACH RUM
			if (!dp.substring(0, 1).equals("Z") && !dp.substring(0, 1).equals("R") && dureesejour >= 2) {
				penalties[i] = penalties[i] + i * 100;
			}
			// ADD 2 POINTS IF DURATION IS O AND 1 IF DURATION IS 1
			if (dureesejour == 1)
				penalties[i] = penalties[i] + 1;
			else if (dureesejour == 0)
				penalties[i] = penalties[i] + 2;
			if (dp.substring(0, 1).equals("Z") || dp.substring(0, 1).equals("R")) {
				// ADD 150 POINTS IF DP IS Z OR R
				penalties[i] = penalties[i] + 150;
			} else if (diagsimprecis.contains(dp)) {
				// IF DP IS NOT Z OR R BUT BELONGS TO DIAGSIMPRECIS ADD 201 POINTS
				penalties[i] = penalties[i] + 201;
			}			
		}
		
		// RETURN THE RUMNB WITH LEAST SCORE
		for (int i = 0 ; i < multirss.size() ; i++) {
			if (stay == null || stay > penalties[i])
				rumnb = i;
		}
		return rumnb;
		
		
	}
	
	public Dictionaries getDicos() {
		return dicos;
	}

	public void setDicos(Dictionaries dicos) {
		this.dicos = dicos;
	}
	
}
