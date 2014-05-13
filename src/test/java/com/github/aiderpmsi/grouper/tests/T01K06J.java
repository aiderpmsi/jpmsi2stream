package com.github.aiderpmsi.grouper.tests;

public class T01K06J extends BaseTest {

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
				{"CodeCCAM", "LCLB001 "},
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
		setRssacte(transform(acte));
		setRssda(transform(da));
		setRssmain(transform(new String[][][] {rssmain}).get(0));
	}
	
}
