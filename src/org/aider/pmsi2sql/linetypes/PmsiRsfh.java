package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public class PmsiRsfh extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRsfh() {
		super("RSFH");

		PmsiInternalElement MyIdMain =new PmsiInternalElement("rsfhid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfh_rsfhid_seq')");
		addChamp(MyIdMain);
		PmsiIndexElement MyIdmainIndex = new PmsiIndexElement("RSFH_rsfhid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdmainIndex.addIndex("rsfhid");
		addChamp(MyIdmainIndex);
		
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		PmsiFkElement MyIdHeaderFK = new PmsiFkElement("RSFH_rsfheaderid_NumFacture_fk", "RSFA", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(H)"));
		
		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSFH_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new PmsiFilePartElement("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		PmsiIndexElement MyNumRSSIndex = new PmsiIndexElement("RSFH_NumRSS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new PmsiFilePartElement("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new PmsiFilePartElement("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		PmsiIndexElement MyNumFactureIndex = new PmsiIndexElement("RSFH_NumFacture_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);

		addChamp(new PmsiFilePartElement("DateDebutSejour", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		PmsiIndexElement MyDateDebutSejourIndex = new PmsiIndexElement("RSFH_DateDebutSejour_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateDebutSejourIndex.addIndex("DateDebutSejour");
		addChamp(MyDateDebutSejourIndex);
		
		addChamp(new PmsiFilePartElement("CodeUCD", PmsiStandardDbTypeEnum.VARCHAR, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("CoefficientFractionnement", PmsiStandardDbTypeEnum.NUMERIC, 5, "(.{5})"));
		addChamp(new PmsiFilePartElement("PrixAchatUnitaire", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("MontantUnitaireEcartIndemnisable", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("MontantTotalEcartIndemnisable", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("Quantite", PmsiStandardDbTypeEnum.NUMERIC, 3, "(.{3})"));
		addChamp(new PmsiFilePartElement("MontantTotalFactureTTC", PmsiStandardDbTypeEnum.NUMERIC, 7, "(.{7})"));
	}
}
