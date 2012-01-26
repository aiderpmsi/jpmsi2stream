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
public class pmsirsfheader extends pmsilinetype {

	/**
	 * Constructeur
	 */
	public pmsirsfheader() {
		super("RSFHeader");
				
		pmsidbinternaldbtype MyIdHeader = new pmsidbinternaldbtype("rsfheaderid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdHeader.setValue("nextval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);

		pmsiindexdbtype MyIdheaderIndex = new pmsiindexdbtype("RSFHeader_rsfheaderid_pidx", pmsiindexdbtype.INDEX_PK);
		MyIdheaderIndex.addIndex("rsfheaderid");
		addChamp(MyIdheaderIndex);

		pmsidbinternaldbtype MyIdInsertion = new pmsidbinternaldbtype("pmsiinsertionid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdInsertion.setValue("currval('pmsiinsertion_pmsiinsertionid_seq')");
		addChamp(MyIdInsertion);
		pmsifkdbtype MyIdInsertionFK = new pmsifkdbtype("RSFHeader_pmsiinsertionid_fk", "pmsiinsertion");
		MyIdInsertionFK.addForeignChamp("pmsiinsertionid", "pmsiinsertionid");
		addChamp(MyIdInsertionFK);
		
		pmsidbinternaldbtype MyDateNow = new pmsidbinternaldbtype("DateAjout", PmsiStandardDbTypeEnum.TIMESTAMP, 0);
		MyDateNow.setValue("NOW()");
		addChamp(MyDateNow);
		pmsiindexdbtype MyDateNowIndex = new pmsiindexdbtype("RSFHeader_DateAjout_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateNowIndex.addIndex("DateAjout");
		addChamp(MyDateNowIndex);

		pmsidbinternaldbtype MyLineCounter = new pmsidbinternaldbtype("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new pmsifiledbtype("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		pmsiindexdbtype MyFinessIndex = new pmsiindexdbtype("RSFHeader_FINESS_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);
		
		addChamp(new pmsifiledbtype("NumLot", PmsiStandardDbTypeEnum.NUMERIC, 3, "(\\d{3})"));
		addChamp(new pmsifiledbtype("StatutJuridique", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new pmsifiledbtype("ModeTarifs", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		
		addChamp(new pmsifiledbtype("DateDebut", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		pmsiindexdbtype MyDateDebutIndex = new pmsiindexdbtype("RSFHeader_DateDebut_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateDebutIndex.addIndex("DateDebut");
		addChamp(MyDateDebutIndex);
		
		addChamp(new pmsifiledbtype("DateFin", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		pmsiindexdbtype MyDateFinIndex = new pmsiindexdbtype("RSFHeader_DateFin_idx", pmsiindexdbtype.INDEX_SIMPLE);
		MyDateFinIndex.addIndex("DateFin");
		addChamp(MyDateFinIndex);
		
		addChamp(new pmsifiledbtype("NbEnregistrements", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new pmsifiledbtype("NbRSS", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new pmsifiledbtype("PremierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new pmsifiledbtype("DernierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new pmsifiledbtype("DernierEnvoi", PmsiStandardDbTypeEnum.CHAR, 7, "(.{1})"));
		
		
	}
	
}
