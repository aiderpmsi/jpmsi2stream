package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

public class pmsirssda extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirssda() {
		super("RSSDA");
				
		pmsidbinternaldbtype MyId = new pmsidbinternaldbtype("idrssda", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyId.setValue("nextval('rssda_idrssda_seq')");
		addChamp(MyId);
		pmsiindexdbtype MyIdrssDaIndex = new pmsiindexdbtype("RSSDA_idrssda_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdrssDaIndex.addIndex("idrssda");
		addChamp(MyIdrssDaIndex);

		pmsidbinternaldbtype MyIdMain = new pmsidbinternaldbtype("idmain", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdMain.setValue("currval('rssmain_idmain_seq')");
		addChamp(MyIdMain);
		pmsifkdbtype MyIdmainFK = new pmsifkdbtype("RSSDA_idmain_fk", "RSSMain", "DEFERRABLE INITIALLY DEFERRED");
		MyIdmainFK.addForeignChamp("idmain", "idmain");
		addChamp(MyIdmainFK);

		addChamp(new pmsifiledbtype("DA", PmsiStandardDbTypeEnum.VARCHAR, 8, "(.{8})"));
	}
}
