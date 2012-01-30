package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

/**
 * Capture l'entête d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class PmsiInsertionResult extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public PmsiInsertionResult(String myFileName) {
		super("pmsiinsertionresult");
				
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("pmsiinsertionresultid", PmsiStandardDbTypeEnum.BIGSERIAL, 0, "NOT NULL");
		MyIdHeader.setValue("nextval('pmsiinsertionresult_pmsiinsertionresultid_seq')");
		addChamp(MyIdHeader);

		pmsiindexdbtype MyIdheaderIndex = new pmsiindexdbtype("pmsiinsertionresult_pmsiinsertionresultidpidx", pmsiindexdbtype.INDEX_PK);
		MyIdheaderIndex.addIndex("pmsiinsertionresultid");
		addChamp(MyIdheaderIndex);

		pmsifiledbtype MyStatus = new pmsifiledbtype("Status", PmsiStandardDbTypeEnum.NUMERIC, 1, "NOT NULL", "");
		MyStatus.setValue("");
		addChamp(MyStatus);
		pmsifiledbtype myLog = new pmsifiledbtype("Log", PmsiStandardDbTypeEnum.TEXT, 0, "NOT NULL", "");
		myLog.setValue("");
		addChamp(myLog);
	}
	
}
