package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

public class PmsiRssDad extends PmsiLineType {
	/**
	 * Constructeur
	 */
	public PmsiRssDad() {
		super("RSSDAD");
		
		PmsiInternalElement MyId = new PmsiInternalElement("idrssdad", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyId.setValue("nextval('rssdad_idrssdad_seq')");
		addChamp(MyId);
		PmsiIndexElement MyIdrssDadIndex = new PmsiIndexElement("RSSDAD_idrssdad_pidx", PmsiIndexElement.INDEX_PK);
		MyIdrssDadIndex.addIndex("idrssdad");
		addChamp(MyIdrssDadIndex);

		PmsiInternalElement MyIdMain = new PmsiInternalElement("idmain", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdMain.setValue("currval('rssmain_idmain_seq')");
		addChamp(MyIdMain);
		PmsiFkElement MyIdmainFK = new PmsiFkElement("RSSDAD_idmain_fk", "RSSMain", "DEFERRABLE INITIALLY DEFERRED");
		MyIdmainFK.addForeignChamp("idmain", "idmain");
		addChamp(MyIdmainFK);

		addChamp(new PmsiFilePartElement("DAD", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
	}

}
