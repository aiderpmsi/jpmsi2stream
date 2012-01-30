package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirssdad extends pmsilinetype {
	/**
	 * Constructeur
	 */
	public pmsirssdad() {
		super("RSSDAD");
		
		pmsidbinternaldbtype MyId = new pmsidbinternaldbtype("idrssdad", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyId.setValue("nextval('rssdad_idrssdad_seq')");
		addChamp(MyId);
		pmsiindexdbtype MyIdrssDadIndex = new pmsiindexdbtype("RSSDAD_idrssdad_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdrssDadIndex.addIndex("idrssdad");
		addChamp(MyIdrssDadIndex);

		pmsidbinternaldbtype MyIdMain = new pmsidbinternaldbtype("idmain", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdMain.setValue("currval('rssmain_idmain_seq')");
		addChamp(MyIdMain);
		pmsifkdbtype MyIdmainFK = new pmsifkdbtype("RSSDAD_idmain_fk", "RSSMain", "DEFERRABLE INITIALLY DEFERRED");
		MyIdmainFK.addForeignChamp("idmain", "idmain");
		addChamp(MyIdmainFK);

		addChamp(new pmsifiledbtype("DAD", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
	}

}
