package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public class PmsiRssActe extends PmsiLineType {

	public PmsiRssActe() {
		super("RSSActe");
				
		PmsiInternalElement MyId = new PmsiInternalElement("rssacteid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyId.setValue("nextval('rssacte_rssacteid_seq')");
		addChamp(MyId);
		PmsiIndexElement MyIdrssDadIndex = new PmsiIndexElement("RSSActe_rssacteid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdrssDadIndex.addIndex("rssacteid");
		addChamp(MyIdrssDadIndex);

		PmsiInternalElement MyIdMain = new PmsiInternalElement("rssmainid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdMain.setValue("currval('rssmain_rssmainid_seq')");
		addChamp(MyIdMain);
		PmsiFkElement MyIdmainFK = new PmsiFkElement("RSSActe_rssmainid_fk", "RSSMain", "DEFERRABLE INITIALLY DEFERRED");
		MyIdmainFK.addForeignChamp("rssmainid", "rssmainid");
		addChamp(MyIdmainFK);

		addChamp(new PmsiFilePartElement("DateRealisation", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new PmsiFilePartElement("CodeCCAM", PmsiStandardDbTypeEnum.VARCHAR, 7, "(.{7})"));
		addChamp(new PmsiFilePartElement("Phase", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Activite", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("ExtensionDoc", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("Modificateurs", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new PmsiFilePartElement("RemboursementExceptionnel", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("AssociationNonPrevue", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new PmsiFilePartElement("NbActes", PmsiStandardDbTypeEnum.NUMERIC, 2, "(\\d{2})"));
	}
}
