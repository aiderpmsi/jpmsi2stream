package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;



public class PmsiRsfm extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRsfm() {
		super("RSFM");

		PmsiInternalElement MyIdMain =new PmsiInternalElement("rsfmid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfm_rsfmid_seq')");
		addChamp(MyIdMain);
		PmsiIndexElement MyIdmainIndex = new PmsiIndexElement("RSFM_rsfmid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdmainIndex.addIndex("rsfmid");
		addChamp(MyIdmainIndex);
		
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		PmsiFkElement MyIdHeaderFK = new PmsiFkElement("RSFM_rsfheaderid_NumFacture_fk", "RSFA", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(M)"));
		
		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSFM_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new PmsiFilePartElement("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		PmsiIndexElement MyNumRSSIndex = new PmsiIndexElement("RSFM_NumRSS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new PmsiFilePartElement("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new PmsiFilePartElement("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		PmsiIndexElement MyNumFactureIndex = new PmsiIndexElement("RSFM_NumFacture_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);
		
		addChamp(new PmsiFilePartElement("ModeTraitement", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("DisciplinePrestation", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("DateActe", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		PmsiIndexElement MyDateActeIndex = new PmsiIndexElement("RSFM_DateActe_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateActeIndex.addIndex("DateActe");
		addChamp(MyDateActeIndex);
		
		addChamp(new PmsiFilePartElement("CodeCCAM", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		PmsiIndexElement MyCodeActeIndex = new PmsiIndexElement("RSFM_CodeCCAM_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyCodeActeIndex.addIndex("CodeCCAM");
		addChamp(MyCodeActeIndex);

		addChamp(new PmsiFilePartElement("ExtensionDocumentaire", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Activite", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Phase", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Modificateur1", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Modificateur2", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Modificateur3", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Modificateur4", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("AssociationNonPrevue", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CodeRemboursementExceptionnel", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("NumDent1", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent2", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent3", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent4", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent5", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent6", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent7", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent8", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent9", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent10", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent11", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent12", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent13", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent14", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent15", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("NumDent16", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
	}

}
