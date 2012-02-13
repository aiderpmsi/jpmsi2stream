package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

public class PmsiRssDa extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRssDa() {
		super("RSSDA");
				
		PmsiInternalElement MyId = new PmsiInternalElement("rssdaid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyId.setValue("nextval('rssda_rssdaid_seq')");
		addChamp(MyId);
		PmsiIndexElement MyIdrssDaIndex = new PmsiIndexElement("RSSDA_rssdaid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdrssDaIndex.addIndex("rssdaid");
		addChamp(MyIdrssDaIndex);

		PmsiInternalElement MyIdMain = new PmsiInternalElement("rssmainid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdMain.setValue("currval('rssmain_rssmainid_seq')");
		addChamp(MyIdMain);
		PmsiFkElement MyIdmainFK = new PmsiFkElement("RSSDA_rssmainid_fk", "RSSMain", "DEFERRABLE INITIALLY DEFERRED");
		MyIdmainFK.addForeignChamp("rssmainid", "rssmainid");
		addChamp(MyIdmainFK);

		addChamp(new PmsiFilePartElement("DA", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
	}
}
