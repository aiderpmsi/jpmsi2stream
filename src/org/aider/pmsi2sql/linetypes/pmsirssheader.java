package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;

/**
 * Capture l'entête d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class pmsirssheader extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirssheader() {
		super("RSSHeader");
				
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("idheader", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdHeader.setValue("nextval('rssheader_idheader_seq')");
		addChamp(MyIdHeader);
		pmsiindexdbtype MyIdheaderIndex = new pmsiindexdbtype("RSSHeader_idheader_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdheaderIndex.addIndex("idheader");
		addChamp(MyIdheaderIndex);

		pmsidbinternaldbtype MyIdInsertion = new pmsidbinternaldbtype("pmsiinsertionid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdInsertion.setValue("currval('pmsiinsertion_pmsiinsertionid_seq')");
		addChamp(MyIdInsertion);
		pmsifkdbtype MyIdInsertionFK = new pmsifkdbtype("RSSHeader_pmsiinsertionid_fk", "pmsiinsertion");
		MyIdInsertionFK.addForeignChamp("pmsiinsertionid", "pmsiinsertionid");
		addChamp(MyIdInsertionFK);

		pmsidbinternaldbtype MyDateNow = new pmsidbinternaldbtype("DateAjout", PmsiStandardDbTypeEnum.TIMESTAMP, 0);
		MyDateNow.setValue("NOW()");
		addChamp(MyDateNow);
		pmsiindexdbtype MyDateNowIndex = new pmsiindexdbtype("RSSHeader_DateAjout_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateNowIndex.addIndex("DateAjout");
		addChamp(MyDateNowIndex);
 
		pmsidbinternaldbtype MyLineCounter = new pmsidbinternaldbtype("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);
		
		addChamp(new pmsifiledbtype("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		pmsiindexdbtype MyFinessIndex = new pmsiindexdbtype("RSSHeader_FINESS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new pmsifiledbtype("NumLot", PmsiStandardDbTypeEnum.NUMERIC, 3, "(\\d{3})"));
		addChamp(new pmsifiledbtype("StatutEtablissement", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		
		addChamp(new pmsifiledbtype("DbtPeriode", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		pmsiindexdbtype MyDbtPeriodeIndex = new pmsiindexdbtype("RSSHeader_DbtPeriode_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDbtPeriodeIndex.addIndex("DbtPeriode");
		addChamp(MyDbtPeriodeIndex);
		
		addChamp(new pmsifiledbtype("FinPeriode", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		pmsiindexdbtype MyFinPeriodeIndex = new pmsiindexdbtype("RSSHeader_FinPeriode_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinPeriodeIndex.addIndex("FinPeriode");
		addChamp(MyFinPeriodeIndex);
		
		addChamp(new pmsifiledbtype("NbEnregistrements", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new pmsifiledbtype("NbRSS", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new pmsifiledbtype("PremierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new pmsifiledbtype("DernierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new pmsifiledbtype("DernierEnvoiTrimestre", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
	}
	
}
