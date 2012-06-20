package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.PmsiLineType;

public class PmsiRsfl2012 extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRsfl2012() {
		super("RSFL2012");

		PmsiInternalElement MyIdMain =new PmsiInternalElement("rsfl2012id", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdMain.setValue("nextval('rsfl2012_rsfl2012id_seq')");
		addChamp(MyIdMain);
		PmsiIndexElement MyIdmainIndex = new PmsiIndexElement("RSFL2012_rsfl2012id_pidx", PmsiIndexElement.INDEX_PK);
		MyIdmainIndex.addIndex("rsfl2012id");
		addChamp(MyIdmainIndex);
		
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rsfheaderid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdHeader.setValue("currval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);
		PmsiFkElement MyIdHeaderFK = new PmsiFkElement("RSFL2012_rsfheaderid_NumFacture_fk", "RSFA", "DEFERRABLE INITIALLY DEFERRED");
		MyIdHeaderFK.addForeignChamp("rsfheaderid", "rsfheaderid");
		MyIdHeaderFK.addForeignChamp("NumFacture", "NumFacture");
		addChamp(MyIdHeaderFK);

		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("TypeEnregistrement", PmsiStandardDbTypeEnum.CHAR, 1, "(L)"));
		
		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSFL2012_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new PmsiFilePartElement("NumRSS", PmsiStandardDbTypeEnum.NUMERIC, 20, "(.{20})"));
		PmsiIndexElement MyNumRSSIndex = new PmsiIndexElement("RSFL2012_NumRSS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumRSSIndex.addIndex("NumRSS");
		addChamp(MyNumRSSIndex);
		
		addChamp(new PmsiFilePartElement("CodeSS", PmsiStandardDbTypeEnum.VARCHAR, 13, "(.{13})"));
		addChamp(new PmsiFilePartElement("CleCodeSS", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("RangBeneficiaire", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));

		addChamp(new PmsiFilePartElement("NumFacture", PmsiStandardDbTypeEnum.NUMERIC, 9, "(.{9})"));
		PmsiIndexElement MyNumFactureIndex = new PmsiIndexElement("RSFH_NumFacture_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyNumFactureIndex.addIndex("NumFacture");
		addChamp(MyNumFactureIndex);

		addChamp(new PmsiFilePartElement("ModeTraitement", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("Discipline", PmsiStandardDbTypeEnum.VARCHAR, 3, "(.{3})"));
		
		addChamp(new PmsiFilePartElement("DateActe1", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		addChamp(new PmsiFilePartElement("QuantiteActe1", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("CodeActe1", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
		
		addChamp(new PmsiFilePartElement("DateActe2", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		addChamp(new PmsiFilePartElement("QuantiteActe2", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("CodeActe2", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));

		addChamp(new PmsiFilePartElement("DateActe3", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		addChamp(new PmsiFilePartElement("QuantiteActe3", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("CodeActe3", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));

		addChamp(new PmsiFilePartElement("DateActe4", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		addChamp(new PmsiFilePartElement("QuantiteActe4", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("CodeActe4", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));

		addChamp(new PmsiFilePartElement("DateActe5", PmsiStandardDbTypeEnum.DATE, 0, "(.{8})"));
		addChamp(new PmsiFilePartElement("QuantiteActe5", PmsiStandardDbTypeEnum.NUMERIC, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("CodeActe5", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));

	}
}
