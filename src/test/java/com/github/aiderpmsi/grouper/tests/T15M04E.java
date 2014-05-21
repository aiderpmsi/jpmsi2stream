package com.github.aiderpmsi.grouper.tests;

import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class T15M04E extends BaseTest {

	private static final long serialVersionUID = -9058623832932246665L;

	private String[][] rssmain = {
			{"DP", "Z082    "},
			{"DR", "B202    "},
			{"NbSeances", "0      "},
			{"ModeSortie", "9 "},
			{"DDN", "2013-04-05"},
			{"DateEntree", "2013-04-08"},
			{"DateSortie", "2013-12-15"}
	};
	
	private String[][][] acte = {
			{
				{"CodeCCAM", "JVJF004 "},
				{"Phase", "0"}
			}
	};
	
	private String[][][] da = {
			{
				{"DA", "A280"},
				{"DA", "Z491"}
			}
	};
	
	public T15M04E() {
		RssContent rss = new RssContent();
		rss.setRssacte(transform(acte));
		rss.setRssda(transform(da));
		rss.setRssmain(transform(new String[][][] {rssmain}).get(0));
		add(rss);
	}
	
}
