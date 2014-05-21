package com.github.aiderpmsi.grouper.tests;

import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class T01K06J extends BaseTest {

	private static final long serialVersionUID = -786049459597255247L;

	private String[][] rssmain = {
			{"DP", "A066     "},
			{"DR", "B202    "},
			{"NbSeances", "0      "},
			{"ModeSortie", "8 "},
			{"DDN", "1980-04-05"},
			{"DateEntree", "2013-04-08"},
			{"DateSortie", "2013-04-08"}
	};
	
	private String[][][] acte = {
			{
				{"CodeCCAM", "JVJF004 "},
				{"Phase", "0"}
			},
			{
				{"CodeCCAM", "ZZLP025 "},
				{"Phase", "0"},
				{"Activite", "4"}
			}
	};
	
	private String[][][] da = {
			{
				{"DA", "A280"},
			},
			{
				{"DA", "Z491"}
			}
	};
	
	public T01K06J() {
		RssContent rss = new RssContent();
		rss.setRssacte(transform(acte));
		rss.setRssda(transform(da));
		rss.setRssmain(transform(new String[][][] {rssmain}).get(0));
		add(rss);
	}
	
}
