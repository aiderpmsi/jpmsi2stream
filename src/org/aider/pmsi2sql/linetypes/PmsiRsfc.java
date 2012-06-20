package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;



public class PmsiRsfc extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRsfc() {
		super("RSFC");

		PmsiInternalElement MyIdMain =new PmsiInternalElement("rsfcid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfc_rsfcid_seq')");
		addChamp(MyIdMain);
		PmsiIndexElement MyIdmainIndex = new PmsiIndexElement("RSFC_rsfcid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdmainIndex.addIndex("rsfcid");
		addChamp(MyIdmainIndex);

		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		PmsiFkElement MyIdHeaderFK = new PmsiFkElement("RSFC_rsfheaderid_NumFacture_fk", "RSFA", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(C)"));
		
		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSFC_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new PmsiFilePartElement("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		PmsiIndexElement MyNumRSSIndex = new PmsiIndexElement("RSFC_NumRSS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new PmsiFilePartElement("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new PmsiFilePartElement("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		PmsiIndexElement MyNumFactureIndex = new PmsiIndexElement("RSFC_NumFacture_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);
		
		addChamp(new PmsiFilePartElement("ModeTraitement", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("DisciplinePrestation", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		
		addChamp(new PmsiFilePartElement("JustifExonerationTM", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));

		addChamp(new PmsiFilePartElement("DateActe", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		PmsiIndexElement MyDateActeIndex = new PmsiIndexElement("RSFC_DateActe_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateActeIndex.addIndex("DateActe");
		addChamp(MyDateActeIndex);

		addChamp(new PmsiFilePartElement("CodeActe", PmsiStandardDbTypeEnum.VARCHAR, 5, "(.{5})"));
		PmsiIndexElement MyCodeActeIndex = new PmsiIndexElement("RSFC_CodeActe_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyCodeActeIndex.addIndex("CodeActe");
		addChamp(MyCodeActeIndex);
		
		addChamp(new PmsiFilePartElement("Quantite", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("Coefficient", PmsiStandardDbTypeEnum.NUMERIC, 6, "(.{6})"));		
		addChamp(new PmsiFilePartElement("Denombrement", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("PrixUnitaire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("MontantBaseRemboursementHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("TauxRemboursement", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new PmsiFilePartElement("MontantRemboursableAMOHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("MontantTotalHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("MontantRemboursableOCHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 6, "(.{6})"));
		addChamp(new PmsiFilePartElement("MontantNOEMIE", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("OperationNOEMIE", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));
 	}

}
