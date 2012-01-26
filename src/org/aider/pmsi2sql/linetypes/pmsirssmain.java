package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirssmain extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirssmain() {
		super("RSSMain");

		pmsidbinternaldbtype MyIdMain =new pmsidbinternaldbtype("idmain", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rssmain_idmain_seq')");
		addChamp(MyIdMain);
		pmsiindexdbtype MyIdmainIndex = new pmsiindexdbtype("RSSMain_idmain_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdmainIndex.addIndex("idmain");
		addChamp(MyIdmainIndex);
		
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("idheader", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rssheader_idheader_seq')");
		addChamp(MyIdHeader);
		pmsifkdbtype MyIdHeaderFK = new pmsifkdbtype("RSSMain_idheader_fk", "RSSHeader");
		MyIdHeaderFK.addForeignChamp("idheader", "idheader");
		addChamp(MyIdHeaderFK);

		pmsidbinternaldbtype MyLineCounter = new pmsidbinternaldbtype("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new pmsifiledbtype("VersionClassification", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumCMD", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumGHM", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new pmsifiledbtype("Filler", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("VersionFormatRSS", PmsiStandardDbTypeEnum.VARCHAR, 3, "(116)"));
		addChamp(new pmsifiledbtype("GroupageCodeRet", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new pmsifiledbtype("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		pmsiindexdbtype MyFinessIndex = new pmsiindexdbtype("RSSMain_FINESS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);
		
		addChamp(new pmsifiledbtype("VersionFormatRUM", PmsiStandardDbTypeEnum.VARCHAR, 3, "(016)"));

		addChamp(new pmsifiledbtype("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		pmsiindexdbtype MyNumRSSIndex = new pmsiindexdbtype("RSSMain_NumRSS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		pmsiindexdbtype MyNumRSSIdheaderUnique = new pmsiindexdbtype("RSSMain_idmain_NumRSS_uidx", pmsiindexdbtype.INDEX_UNIQUE);
		MyNumRSSIdheaderUnique.addIndex("idmain");
		MyNumRSSIdheaderUnique.addIndex("NumRSS");
		addChamp(MyNumRSSIdheaderUnique);
		
		addChamp(new pmsifiledbtype("NumLocalSejour", PmsiStandardDbTypeEnum.VARCHAR, 20, "(.{20})"));

		addChamp(new pmsifiledbtype("NumRUM", PmsiStandardDbTypeEnum.NUMERIC, 10, "(.{10})"));
		pmsiindexdbtype MyNumRUMIndex = new pmsiindexdbtype("RSSMain_NumRUM_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumRUMIndex.addIndex("NumRUM");
		addChamp(MyNumRUMIndex);
		
		addChamp(new pmsifiledbtype("DDN", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new pmsifiledbtype("Sexe", PmsiStandardDbTypeEnum.NUMERIC, 1, "([1|2])"));
		addChamp(new pmsifiledbtype("NumUniteMedicale", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new pmsifiledbtype("TypeAutorisationLit", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("DateEntree", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new pmsifiledbtype("ModeEntree", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Provenance", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("DateSortie", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new pmsifiledbtype("ModeSortie", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Destination", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("CPResidence", PmsiStandardDbTypeEnum.VARCHAR, 5, "(.{5})"));
		addChamp(new pmsifiledbtype("PoidsNouveauNe", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new pmsifiledbtype("AgeGestationnel", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("DDRegles", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8}|[ ]{8})"));
		addChamp(new pmsifiledbtype("NbSeances", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NbDA", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NbDAD", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NbZA", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new pmsifiledbtype("DP", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("DR", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("IGS2", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new pmsifiledbtype("ConfCodageRSS", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("TypeMachineRadiotherapie", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("TypeDosimetrie", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("NumInnovation", PmsiStandardDbTypeEnum.NUMERIC, 15, "(.{15})"));
		addChamp(new pmsifiledbtype("ZoneReservee", PmsiStandardDbTypeEnum.VARCHAR, 15, "(.{15})"));
	}

}
