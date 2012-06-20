package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.PmsiLineType;

public class PmsiRsfb extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRsfb() {
		super("RSFB");

		PmsiInternalElement MyIdMain =new PmsiInternalElement("rsfbid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfb_rsfbid_seq')");
		addChamp(MyIdMain);
		PmsiIndexElement MyIdmainIndex = new PmsiIndexElement("RSFB_rsfbid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdmainIndex.addIndex("rsfbid");
		addChamp(MyIdmainIndex);
		
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		PmsiFkElement MyIdHeaderFK = new PmsiFkElement("RSFB_rsfheaderid_NumFacture_fk", "RSFA", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(B)"));
		
		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSFB_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new PmsiFilePartElement("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		PmsiIndexElement MyNumRSSIndex = new PmsiIndexElement("RSFB_NumRSS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new PmsiFilePartElement("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new PmsiFilePartElement("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		PmsiIndexElement MyNumFactureIndex = new PmsiIndexElement("RSFB_NumFacture_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);
		
		addChamp(new PmsiFilePartElement("ModeTraitement", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("DisciplinePrestation", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("DateDebutSejour", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		PmsiIndexElement MyDateDebutSejourIndex = new PmsiIndexElement("RSFB_DateDebutSejour_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateDebutSejourIndex.addIndex("DateDebutSejour");
		addChamp(MyDateDebutSejourIndex);
		
		addChamp(new PmsiFilePartElement("DateFinSejour", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		PmsiIndexElement MyDateFinSejourIndex = new PmsiIndexElement("RSFB_DateFinSejour_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateFinSejourIndex.addIndex("DateFinSejour");
		addChamp(MyDateFinSejourIndex);

		addChamp(new PmsiFilePartElement("CodeActe", PmsiStandardDbTypeEnum.VARCHAR, 5, "(.{5})"));
		PmsiIndexElement MyCodeActeIndex = new PmsiIndexElement("RSFB_CodeActe_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyCodeActeIndex.addIndex("CodeActe");
		addChamp(MyCodeActeIndex);
		
		addChamp(new PmsiFilePartElement("Quantite", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new PmsiFilePartElement("JustifExonerationTM", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Coefficient", PmsiStandardDbTypeEnum.NUMERIC, 5, "(.{5})"));
		addChamp(new PmsiFilePartElement("CodePEC", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("CoefficientMCO", PmsiStandardDbTypeEnum.NUMERIC, 5, "(.{5})"));
		addChamp(new PmsiFilePartElement("PrixUnitaire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("MontantBaseRemboursementPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("TauxPrestation", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new PmsiFilePartElement("MontantRemboursableAMOPH", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("MontantTotalDepense", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("MontantRemboursableOCPH", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("NumGHS", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new PmsiFilePartElement("MontantNOEMIE", PmsiStandardDbTypeEnum.NUMERIC, 8, "(.{8})"));
		addChamp(new PmsiFilePartElement("OperationNOEMIE", PmsiStandardDbTypeEnum.CHAR, 3, "(.{3})"));
	}

}
