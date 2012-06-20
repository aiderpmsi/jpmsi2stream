package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;



public class PmsiRsfa2012 extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRsfa2012() {
		super("RSFA2012");

		PmsiInternalElement MyIdMain =new PmsiInternalElement("rsfa2012id", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfa_rsfaid_seq')");
		addChamp(MyIdMain);
		PmsiIndexElement MyIdmainIndex = new PmsiIndexElement("RSFA2012_rsfa2012id_pidx", PmsiIndexElement.INDEX_PK);
		MyIdmainIndex.addIndex("rsfa2012id");
		addChamp(MyIdmainIndex);
		
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		PmsiFkElement MyIdHeaderFK = new PmsiFkElement("RSFA2012_rsfheaderid_fk", "RSFHeader", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		addChamp(MyIdHeaderFK);
		
		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(A)"));
		
		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSFA2012_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new PmsiFilePartElement("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		PmsiIndexElement MyNumRSSIndex = new PmsiIndexElement("RSFA2012_NumRSS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new PmsiFilePartElement("Sexe", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CodeCivilite", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new PmsiFilePartElement("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));
		
		addChamp(new PmsiFilePartElement("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		PmsiIndexElement MyNumFactureIndex = new PmsiIndexElement("RSFA2012_NumFacture_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);
		
		PmsiIndexElement MyUniqueNumFactureIndex = new PmsiIndexElement("RSFA2012_rsfheaderid_NumFacture_idx", PmsiIndexElement.INDEX_UNIQUE);
		MyUniqueNumFactureIndex.addIndex("rsfheaderid");
		MyUniqueNumFactureIndex.addIndex("NumFacture");
		addChamp(MyUniqueNumFactureIndex);
		
		addChamp(new PmsiFilePartElement("NatureOperation", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("NatureAssurance", PmsiStandardDbTypeEnum.CHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("TypeContratOC", PmsiStandardDbTypeEnum.CHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("JustifExonerationTM", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CodePEC", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CodeGdRegime", PmsiStandardDbTypeEnum.CHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("DateNaissance", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		addChamp(new PmsiFilePartElement("RangNaissance", PmsiStandardDbTypeEnum.NUMERIC, 1, "(.{1})"));
		
		addChamp(new PmsiFilePartElement("DateEntree", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		PmsiIndexElement MyDateEntreeIndex = new PmsiIndexElement("RSFA2012_DateEntree_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateEntreeIndex.addIndex("DateEntree");
		addChamp(MyDateEntreeIndex);
		
		addChamp(new PmsiFilePartElement("DateSortie", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		PmsiIndexElement MyDateSortieIndex = new PmsiIndexElement("RSFA2012_DateSortie_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateSortieIndex.addIndex("DateSortie");
		addChamp(MyDateSortieIndex);
		
		addChamp(new PmsiFilePartElement("TotalBaseRemboursementPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TotalRemboursableAMOPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TotalFactureHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TotalRemboursableAMOHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TotalParticipationAvantOC", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TotalRemboursableOCPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TotalRemboursableOCHonoraire", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TotalFacturePH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("EtatLiquidation", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CMU", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("LienMere", PmsiStandardDbTypeEnum.VARCHAR, 9, "(.{9})"));
	}

}
