package com.github.aiderpmsi.pims.grouper.model;

public enum RssMain {

	nbseances,
	dp,
	dr,
	modeentree,
	modesortie,
	poidsnouveaune,
	sexe,
	dateentree,
	datesortie,
	ddn,
	agegestationnel;
	
	// USED BECAUSE JEXL CAN NOT ACCES ENUMS
	public RssMain nbseances() {return RssMain.nbseances;}
	public RssMain dp() {return RssMain.dp;}
	public RssMain dr() {return RssMain.dr;}
	public RssMain modeentree() {return RssMain.modeentree;}
	public RssMain modesortie() {return RssMain.modesortie;}
	public RssMain poidsnouveaune() {return RssMain.poidsnouveaune;}
	public RssMain sexe() {return RssMain.sexe;}
	public RssMain dateentree() {return RssMain.dateentree;}
	public RssMain datesortie() {return RssMain.datesortie;}
	public RssMain ddn() {return RssMain.ddn;}
	public RssMain agegestationnel() {return RssMain.agegestationnel;}
}
