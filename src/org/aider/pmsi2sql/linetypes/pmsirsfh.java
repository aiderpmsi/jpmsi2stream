package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirsfh extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirsfh() {
		super("RSFH");

		pmsidbinternaldbtype MyIdMain =new pmsidbinternaldbtype("rsfhid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfh_rsfhid_seq')");
		addChamp(MyIdMain);
		pmsiindexdbtype MyIdmainIndex = new pmsiindexdbtype("RSFH_rsfhid_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdmainIndex.addIndex("rsfhid");
		addChamp(MyIdmainIndex);
		
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		pmsifkdbtype MyIdHeaderFK = new pmsifkdbtype("RSFH_rsfheaderid_NumFacture_fk", "RSFA");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		pmsidbinternaldbtype MyLineCounter = new pmsidbinternaldbtype("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new pmsifiledbtype("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(H)"));
		
		addChamp(new pmsifiledbtype("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		pmsiindexdbtype MyFinessIndex = new pmsiindexdbtype("RSFH_FINESS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new pmsifiledbtype("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		pmsiindexdbtype MyNumRSSIndex = new pmsiindexdbtype("RSFH_NumRSS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new pmsifiledbtype("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new pmsifiledbtype("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new pmsifiledbtype("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		pmsiindexdbtype MyNumFactureIndex = new pmsiindexdbtype("RSFH_NumFacture_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);

		addChamp(new pmsifiledbtype("DateDebutSejour", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		pmsiindexdbtype MyDateDebutSejourIndex = new pmsiindexdbtype("RSFH_DateDebutSejour_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateDebutSejourIndex.addIndex("DateDebutSejour");
		addChamp(MyDateDebutSejourIndex);
		
		addChamp(new pmsifiledbtype("CodeUCD", PmsiStandardDbTypeEnum.VARCHAR, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("CoefficientFractionnement", PmsiStandardDbTypeEnum.NUMERIC, 5, "(.{5})"));
		addChamp(new pmsifiledbtype("PrixAchatUnitaire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("MontantUnitaireEcartIndemnisable", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("MontantTotalEcartIndemnisable", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("Quantite", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new pmsifiledbtype("MontantTotalFactureTTC", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
	}
}
