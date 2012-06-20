package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;



public class PmsiRssMain extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRssMain() {
		super("RSSMain");

		PmsiInternalElement MyIdMain =new PmsiInternalElement("rssmainid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rssmain_rssmainid_seq')");
		addChamp(MyIdMain);
		PmsiIndexElement MyIdmainIndex = new PmsiIndexElement("RSSMain_rssmainid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdmainIndex.addIndex("rssmainid");
		addChamp(MyIdmainIndex);
		
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rssheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rssheader_rssheaderid_seq')");
		addChamp(MyIdHeader);
		PmsiFkElement MyIdHeaderFK = new PmsiFkElement("RSSMain_rssheaderid_fk", "RSSHeader", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rssheaderid", "rssheaderid");
		addChamp(MyIdHeaderFK);

		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("VersionClassification", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumCMD", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumGHM", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new PmsiFilePartElement("Filler", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("VersionFormatRSS", PmsiStandardDbTypeEnum.VARCHAR, 3, "(116)"));
		addChamp(new PmsiFilePartElement("GroupageCodeRet", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSSMain_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);
		
		addChamp(new PmsiFilePartElement("VersionFormatRUM", PmsiStandardDbTypeEnum.VARCHAR, 3, "(016)"));

		addChamp(new PmsiFilePartElement("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		PmsiIndexElement MyNumRSSIndex = new PmsiIndexElement("RSSMain_NumRSS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		PmsiIndexElement MyNumRSSIdheaderUnique = new PmsiIndexElement("RSSMain_rssmainid_NumRSS_uidx", PmsiIndexElement.INDEX_UNIQUE);
		MyNumRSSIdheaderUnique.addIndex("rssmainid");
		MyNumRSSIdheaderUnique.addIndex("NumRSS");
		addChamp(MyNumRSSIdheaderUnique);
		
		addChamp(new PmsiFilePartElement("NumLocalSejour", PmsiStandardDbTypeEnum.VARCHAR, 20, "(.{20})"));

		addChamp(new PmsiFilePartElement("NumRUM", PmsiStandardDbTypeEnum.NUMERIC, 10, "(.{10})"));
		PmsiIndexElement MyNumRUMIndex = new PmsiIndexElement("RSSMain_NumRUM_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRUMIndex.addIndex("NumRUM");
		addChamp(MyNumRUMIndex);
		
		addChamp(new PmsiFilePartElement("DDN", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new PmsiFilePartElement("Sexe", PmsiStandardDbTypeEnum.NUMERIC, 1, "([1|2])"));
		addChamp(new PmsiFilePartElement("NumUniteMedicale", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new PmsiFilePartElement("TypeAutorisationLit", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("DateEntree", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new PmsiFilePartElement("ModeEntree", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Provenance", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("DateSortie", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new PmsiFilePartElement("ModeSortie", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Destination", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CPResidence", PmsiStandardDbTypeEnum.VARCHAR, 5, "(.{5})"));
		addChamp(new PmsiFilePartElement("PoidsNouveauNe", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new PmsiFilePartElement("AgeGestationnel", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("DDRegles", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8}|[ ]{8})"));
		addChamp(new PmsiFilePartElement("NbSeances", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NbDA", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NbDAD", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NbZA", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new PmsiFilePartElement("DP", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("DR", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("IGS2", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new PmsiFilePartElement("ConfCodageRSS", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("TypeMachineRadiotherapie", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("TypeDosimetrie", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("NumInnovation", PmsiStandardDbTypeEnum.NUMERIC, 15, "(.{15})"));
		addChamp(new PmsiFilePartElement("ZoneReservee", PmsiStandardDbTypeEnum.VARCHAR, 15, "(.{15})"));
	}

}
