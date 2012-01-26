package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirssacte extends pmsilinetype {

	public pmsirssacte() {
		super("RSSActe");
				
		pmsidbinternaldbtype MyId = new pmsidbinternaldbtype("idrssacte", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyId.setValue("nextval('rssacte_idrssacte_seq')");
		addChamp(MyId);
		pmsiindexdbtype MyIdrssDadIndex = new pmsiindexdbtype("RSSActe_idrssacte_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdrssDadIndex.addIndex("idrssacte");
		addChamp(MyIdrssDadIndex);

		pmsidbinternaldbtype MyIdMain = new pmsidbinternaldbtype("idmain", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdMain.setValue("currval('rssmain_idmain_seq')");
		addChamp(MyIdMain);
		pmsifkdbtype MyIdmainFK = new pmsifkdbtype("RSSActe_idmain_fk", "RSSMain");
		MyIdmainFK.addForeignChamp("idmain", "idmain");
		addChamp(MyIdmainFK);

		addChamp(new pmsifiledbtype("DateRealisation", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		addChamp(new pmsifiledbtype("CodeCCAM", PmsiStandardDbTypeEnum.VARCHAR, 7, "(.{7})"));
		addChamp(new pmsifiledbtype("Phase", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Activite", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("ExtensionDoc", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("Modificateurs", PmsiStandardDbTypeEnum.VARCHAR, 4, "(.{4})"));
		addChamp(new pmsifiledbtype("RemboursementExceptionnel", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("AssociationNonPrevue", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
		addChamp(new pmsifiledbtype("NbActes", PmsiStandardDbTypeEnum.NUMERIC, 2, "(\\d{2})"));
	}
}
