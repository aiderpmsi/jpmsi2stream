package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

/**
 * Capture l'ent�te d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class PmsiInsertion extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public PmsiInsertion(String myFileName) {
		super("pmsiinsertion");
				
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("pmsiinsertionid", PmsiStandardDbTypeEnum.BIGSERIAL, 0, "NOT NULL");
		MyIdHeader.setValue("nextval('pmsiinsertion_pmsiinsertionid_seq')");
		addChamp(MyIdHeader);

		pmsiindexdbtype MyIdheaderIndex = new pmsiindexdbtype("pmsiinsertion_pmsiinsertionidpidx", pmsiindexdbtype.INDEX_PK);
		MyIdheaderIndex.addIndex("pmsiinsertionid");
		addChamp(MyIdheaderIndex);

		pmsidbinternaldbtype MyDateNow = new pmsidbinternaldbtype("DateAjout", PmsiStandardDbTypeEnum.TIMESTAMP, 0, "DEFAULT NOW() NOT NULL INITIALLY DEFERRED");
		MyDateNow.setValue("NOW()");
		addChamp(MyDateNow);
		pmsiindexdbtype MyDateNowIndex = new pmsiindexdbtype("pmsiinsertion_DateAjout_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateNowIndex.addIndex("DateAjout");
		addChamp(MyDateNowIndex);

		pmsifiledbtype MyFileName = new pmsifiledbtype("NomFichier", PmsiStandardDbTypeEnum.TEXT, 0, "NOT NULL", "");
		MyFileName.setValue(myFileName);
		addChamp(MyFileName);

		pmsifiledbtype MyFile = new pmsifiledbtype("Fichier", PmsiStandardDbTypeEnum.FILE, 0, "NOT NULL INITIALLY DEFERRED", "");
		MyFile.setValue(myFileName);
		addChamp(MyFile);

		pmsifiledbtype MyStatus = new pmsifiledbtype("Status", PmsiStandardDbTypeEnum.NUMERIC, 1, "NOT NULL INITIALLY DEFERRED", "");
		MyStatus.setValue("");
		addChamp(MyStatus);
		pmsifiledbtype myLog = new pmsifiledbtype("Log", PmsiStandardDbTypeEnum.TEXT, 0, "NOT NULL INITIALLY DEFERRED", "");
		myLog.setValue("");
		addChamp(myLog);
	}
	
}
