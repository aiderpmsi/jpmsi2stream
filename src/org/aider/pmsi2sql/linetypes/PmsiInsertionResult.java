package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.PmsiLineType;

/**
 * Stocke le statut d'une insertion de fichier pmsi 
 * @author delabre
 *
 */
public class PmsiInsertionResult extends PmsiLineType {

	/**
	 * Constructeur
	 * @param myStatusString chaine de caract�res � stocker dans le statut de l'insertino (r�ussi / �chou�)
	 * @param myLogString log de l'insertion
	 */
	public PmsiInsertionResult(String myStatusString, String myLogString) {
		super("pmsiinsertionresult");
				
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("pmsiinsertionresultid", PmsiStandardDbTypeEnum.BIGSERIAL, 0, "NOT NULL");
		MyIdHeader.setValue("nextval('pmsiinsertionresult_pmsiinsertionresultid_seq')");
		addChamp(MyIdHeader);

		PmsiIndexElement MyIdheaderIndex = new PmsiIndexElement("pmsiinsertionresult_pmsiinsertionresultidpidx", PmsiIndexElement.INDEX_PK);
		MyIdheaderIndex.addIndex("pmsiinsertionresultid");
		addChamp(MyIdheaderIndex);

		PmsiFilePartElement MyStatus = new PmsiFilePartElement("Status", PmsiStandardDbTypeEnum.NUMERIC, 1, "NOT NULL", "");
		MyStatus.setValue(myStatusString);
		addChamp(MyStatus);
		PmsiFilePartElement myLog = new PmsiFilePartElement("Log", PmsiStandardDbTypeEnum.TEXT, 0, "NOT NULL", "");
		myLog.setValue(myLogString);
		addChamp(myLog);
	}
	
}
