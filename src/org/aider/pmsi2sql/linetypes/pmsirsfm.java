package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirsfm extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirsfm() {
		super("RSFM");

		pmsidbinternaldbtype MyIdMain =new pmsidbinternaldbtype("rsfmid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfm_rsfmid_seq')");
		addChamp(MyIdMain);
		pmsiindexdbtype MyIdmainIndex = new pmsiindexdbtype("RSFM_rsfmid_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdmainIndex.addIndex("rsfmid");
		addChamp(MyIdmainIndex);
		
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		pmsifkdbtype MyIdHeaderFK = new pmsifkdbtype("RSFM_rsfheaderid_NumFacture_fk", "RSFA");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		pmsidbinternaldbtype MyLineCounter = new pmsidbinternaldbtype("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new pmsifiledbtype("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(M)"));
		
		addChamp(new pmsifiledbtype("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		pmsiindexdbtype MyFinessIndex = new pmsiindexdbtype("RSFM_FINESS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new pmsifiledbtype("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		pmsiindexdbtype MyNumRSSIndex = new pmsiindexdbtype("RSFM_NumRSS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new pmsifiledbtype("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new pmsifiledbtype("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new pmsifiledbtype("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		pmsiindexdbtype MyNumFactureIndex = new pmsiindexdbtype("RSFM_NumFacture_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);
		
		addChamp(new pmsifiledbtype("ModeTraitement", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("DisciplinePrestation", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));

		addChamp(new pmsifiledbtype("DateActe", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		pmsiindexdbtype MyDateActeIndex = new pmsiindexdbtype("RSFM_DateActe_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateActeIndex.addIndex("DateActe");
		addChamp(MyDateActeIndex);
		
		addChamp(new pmsifiledbtype("CodeCCAM", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		pmsiindexdbtype MyCodeActeIndex = new pmsiindexdbtype("RSFM_CodeCCAM_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyCodeActeIndex.addIndex("CodeCCAM");
		addChamp(MyCodeActeIndex);

		addChamp(new pmsifiledbtype("ExtensionDocumentaire", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Activite", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Phase", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Modificateur1", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Modificateur2", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Modificateur3", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Modificateur4", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("AssociationNonPrevue", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("CodeRemboursementExceptionnel", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("NumDent1", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent2", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent3", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent4", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent5", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent6", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent7", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent8", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent9", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent10", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent11", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent12", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent13", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent14", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent15", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("NumDent16", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
	}

}
