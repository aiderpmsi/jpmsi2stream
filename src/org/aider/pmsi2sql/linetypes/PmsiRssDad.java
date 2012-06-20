package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public class PmsiRssDad extends PmsiLineType {
	/**
	 * Constructeur
	 */
	public PmsiRssDad() {
		super("RSSDAD");
		
		PmsiInternalElement MyId = new PmsiInternalElement("rssdadid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyId.setValue("nextval('rssdad_rssdadid_seq')");
		addChamp(MyId);
		PmsiIndexElement MyIdrssDadIndex = new PmsiIndexElement("RSSDAD_rssdadid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdrssDadIndex.addIndex("rssdadid");
		addChamp(MyIdrssDadIndex);

		PmsiInternalElement MyIdMain = new PmsiInternalElement("rssmainid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdMain.setValue("currval('rssmain_rssmainid_seq')");
		addChamp(MyIdMain);
		PmsiFkElement MyIdmainFK = new PmsiFkElement("RSSDAD_rssmainid_fk", "RSSMain", "DEFERRABLE INITIALLY DEFERRED");
		MyIdmainFK.addForeignChamp("rssmainid", "rssmainid");
		addChamp(MyIdmainFK);

		addChamp(new PmsiFilePartElement("DAD", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
	}

}
