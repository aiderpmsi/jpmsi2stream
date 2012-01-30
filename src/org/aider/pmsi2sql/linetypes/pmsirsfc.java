package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirsfc extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirsfc() {
		super("RSFC");

		pmsidbinternaldbtype MyIdMain =new pmsidbinternaldbtype("rsfcid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfc_rsfcid_seq')");
		addChamp(MyIdMain);
		pmsiindexdbtype MyIdmainIndex = new pmsiindexdbtype("RSFC_rsfcid_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdmainIndex.addIndex("rsfcid");
		addChamp(MyIdmainIndex);

		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		pmsifkdbtype MyIdHeaderFK = new pmsifkdbtype("RSFC_rsfheaderid_NumFacture_fk", "RSFA", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		pmsidbinternaldbtype MyLineCounter = new pmsidbinternaldbtype("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new pmsifiledbtype("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(C)"));
		
		addChamp(new pmsifiledbtype("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		pmsiindexdbtype MyFinessIndex = new pmsiindexdbtype("RSFC_FINESS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new pmsifiledbtype("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		pmsiindexdbtype MyNumRSSIndex = new pmsiindexdbtype("RSFC_NumRSS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new pmsifiledbtype("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new pmsifiledbtype("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new pmsifiledbtype("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		pmsiindexdbtype MyNumFactureIndex = new pmsiindexdbtype("RSFC_NumFacture_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);
		
		addChamp(new pmsifiledbtype("ModeTraitement", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("DisciplinePrestation", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		
		addChamp(new pmsifiledbtype("JustifExonerationTM", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));

		addChamp(new pmsifiledbtype("DateActe", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		pmsiindexdbtype MyDateActeIndex = new pmsiindexdbtype("RSFC_DateActe_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateActeIndex.addIndex("DateActe");
		addChamp(MyDateActeIndex);

		addChamp(new pmsifiledbtype("CodeActe", PmsiStandardDbTypeEnum.VARCHAR, 5, "(.{5})"));
		pmsiindexdbtype MyCodeActeIndex = new pmsiindexdbtype("RSFC_CodeActe_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyCodeActeIndex.addIndex("CodeActe");
		addChamp(MyCodeActeIndex);
		
		addChamp(new pmsifiledbtype("Quantite", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("Coefficient", PmsiStandardDbTypeEnum.NUMERIC, 6, "(.{6})"));		
		addChamp(new pmsifiledbtype("Denombrement", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("PrixUnitaire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("MontantBaseRemboursementHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("TauxRemboursement", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new pmsifiledbtype("MontantRemboursableAMOHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("MontantTotalHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("MontantRemboursableOCHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 6, "(.{6})"));
		addChamp(new pmsifiledbtype("MontantNOEMIE", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new pmsifiledbtype("OperationNOEMIE", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));
 	}

}
