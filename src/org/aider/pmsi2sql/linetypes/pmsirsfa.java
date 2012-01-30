package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirsfa extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirsfa() {
		super("RSFA");

		pmsidbinternaldbtype MyIdMain =new pmsidbinternaldbtype("rsfaid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfa_rsfaid_seq')");
		addChamp(MyIdMain);
		pmsiindexdbtype MyIdmainIndex = new pmsiindexdbtype("RSFA_rsfaid_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdmainIndex.addIndex("rsfaid");
		addChamp(MyIdmainIndex);
		
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		pmsifkdbtype MyIdHeaderFK = new pmsifkdbtype("RSFA_rsfheaderid_fk", "RSFHeader", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		addChamp(MyIdHeaderFK);
		
		pmsidbinternaldbtype MyLineCounter = new pmsidbinternaldbtype("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new pmsifiledbtype("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(A)"));
		
		addChamp(new pmsifiledbtype("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		pmsiindexdbtype MyFinessIndex = new pmsiindexdbtype("RSFA_FINESS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new pmsifiledbtype("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		pmsiindexdbtype MyNumRSSIndex = new pmsiindexdbtype("RSFA_NumRSS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new pmsifiledbtype("Sexe", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("CodeCivilite", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new pmsifiledbtype("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));
		
		addChamp(new pmsifiledbtype("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		pmsiindexdbtype MyNumFactureIndex = new pmsiindexdbtype("RSFA_NumFacture_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);
		
		pmsiindexdbtype MyUniqueNumFactureIndex = new pmsiindexdbtype("RSFA_rsfheaderid_NumFacture_idx", pmsiindexdbtype.INDEX_UNIQUE);
		MyUniqueNumFactureIndex.addIndex("rsfheaderid");
		MyUniqueNumFactureIndex.addIndex("NumFacture");
		addChamp(MyUniqueNumFactureIndex);
		
		addChamp(new pmsifiledbtype("NatureOperation", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("NatureAssurance", PmsiStandardDbTypeEnum.CHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("TypeContratOC", PmsiStandardDbTypeEnum.CHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("JustifExonerationTM", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("CodePEC", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("CodeGdRegime", PmsiStandardDbTypeEnum.CHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("DateNaissance", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		addChamp(new pmsifiledbtype("RangNaissance", PmsiStandardDbTypeEnum.NUMERIC, 1, "(.{1})"));
		
		addChamp(new pmsifiledbtype("DateEntree", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		pmsiindexdbtype MyDateEntreeIndex = new pmsiindexdbtype("RSFA_DateEntree_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateEntreeIndex.addIndex("DateEntree");
		addChamp(MyDateEntreeIndex);
		
		addChamp(new pmsifiledbtype("DateSortie", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		pmsiindexdbtype MyDateSortieIndex = new pmsiindexdbtype("RSFA_DateSortie_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateSortieIndex.addIndex("DateSortie");
		addChamp(MyDateSortieIndex);
		
		addChamp(new pmsifiledbtype("TotalBaseRemboursementPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("TotalRemboursableAMOPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("TotalFactureHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("TotalRemboursableAMOHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("TotalParticipationAvantOC", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("TotalRemboursableOCPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("TotalRemboursableOCHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("TotalFacturePH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("EtatLiquidation", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
	}

}
