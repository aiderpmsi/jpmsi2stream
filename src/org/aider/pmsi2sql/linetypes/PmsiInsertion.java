package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Capture l'ent�te d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class PmsiInsertion extends PmsiLineType {

	/**
	 * Constructeur
	 * @param myFileName nom du fichier � partir duquel ce rss est lu
	 */
	public PmsiInsertion(String myFileName) {
		super("pmsiinsertion");
				
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("pmsiinsertionid", PmsiStandardDbTypeEnum.BIGSERIAL, 0, "NOT NULL");
		MyIdHeader.setValue("nextval('pmsiinsertion_pmsiinsertionid_seq')");
		addChamp(MyIdHeader);

		PmsiIndexElement MyIdheaderIndex = new PmsiIndexElement("pmsiinsertion_pmsiinsertionidpidx", PmsiIndexElement.INDEX_PK);
		MyIdheaderIndex.addIndex("pmsiinsertionid");
		addChamp(MyIdheaderIndex);

		PmsiInternalElement MyDateNow = new PmsiInternalElement("DateAjout", PmsiStandardDbTypeEnum.TIMESTAMP, 0, "DEFAULT NOW() NOT NULL INITIALLY DEFERRED");
		MyDateNow.setValue("NOW()");
		addChamp(MyDateNow);
		PmsiIndexElement MyDateNowIndex = new PmsiIndexElement("pmsiinsertion_DateAjout_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateNowIndex.addIndex("DateAjout");
		addChamp(MyDateNowIndex);

		PmsiFilePartElement MyFileName = new PmsiFilePartElement("NomFichier", PmsiStandardDbTypeEnum.TEXT, 0, "NOT NULL", "");
		MyFileName.setValue(myFileName);
		addChamp(MyFileName);

		PmsiFilePartElement MyFile = new PmsiFilePartElement("Fichier", PmsiStandardDbTypeEnum.FILE, 0, "NOT NULL", "");
		MyFile.setValue(myFileName);
		addChamp(MyFile);

		PmsiInternalElement MyIdResult = new PmsiInternalElement("pmsiinsertionresultid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdResult.setValue("-1");
		addChamp(MyIdResult);
		PmsiFkElement MyIdResultFK = new PmsiFkElement("pmsiinsertion_pmsiinsertionresultid_fk", "pmsiinsertionresult", "DEFERRABLE INITIALLY DEFERRED");
		MyIdResultFK.addForeignChamp("pmsiinsertionresultid", "pmsiinsertionresultid");
		addChamp(MyIdResultFK);
	}
}
