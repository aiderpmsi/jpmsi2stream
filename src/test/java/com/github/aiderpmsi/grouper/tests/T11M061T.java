package com.github.aiderpmsi.grouper.tests;

import com.github.aiderpmsi.pims.grouper.model.RssActe;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.RssMain;

public class T11M061T extends BaseTest {

	private static final long serialVersionUID = -8286176082721327295L;

	private Object[][] rssmain = {
			{RssMain.dp, "Z491     "},
			{RssMain.dr, "       "},
			{RssMain.nbseances, "00     "},
			{RssMain.modeentree, "8 "},
			{RssMain.modesortie, "8 "},
			{RssMain.ddn, "16071952"},
			{RssMain.dateentree, "04102013"},
			{RssMain.datesortie, "04102013"},
			{RssMain.poidsnouveaune, "0000"},
			{RssMain.sexe, "1"},
			{RssMain.agegestationnel, "00"}
	};

	private Object[][][] acte = {
			{
				{RssActe.codeccam, "JVJF000 "},
				{RssActe.phase, "0"},
				{RssActe.activite, "1"}
			}
	};
	
	private Object[][][] da = {
			{
			}
	};
	
	public T11M061T() {
		RssContent rss = buildRssContent(rssmain, acte, da);
		add(rss);
	}
	
}
