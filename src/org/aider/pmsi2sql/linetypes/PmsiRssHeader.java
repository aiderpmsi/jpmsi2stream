package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

/**
 * Capture l'entête d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class PmsiRssHeader extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRssHeader() {
		super("RSSHeader");
				
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("idheader", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdHeader.setValue("nextval('rssheader_idheader_seq')");
		addChamp(MyIdHeader);
		PmsiIndexElement MyIdheaderIndex = new PmsiIndexElement("RSSHeader_idheader_pidx", PmsiIndexElement.INDEX_PK);
		MyIdheaderIndex.addIndex("idheader");
		addChamp(MyIdheaderIndex);

		PmsiInternalElement MyIdInsertion = new PmsiInternalElement("pmsiinsertionid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdInsertion.setValue("currval('pmsiinsertion_pmsiinsertionid_seq')");
		addChamp(MyIdInsertion);
		PmsiFkElement MyIdInsertionFK = new PmsiFkElement("RSSHeader_pmsiinsertionid_fk", "pmsiinsertion", "DEFERRABLE INITIALLY DEFERRED");
		MyIdInsertionFK.addForeignChamp("pmsiinsertionid", "pmsiinsertionid");
		addChamp(MyIdInsertionFK);

		PmsiInternalElement MyDateNow = new PmsiInternalElement("DateAjout", PmsiStandardDbTypeEnum.TIMESTAMP, 0);
		MyDateNow.setValue("NOW()");
		addChamp(MyDateNow);
		PmsiIndexElement MyDateNowIndex = new PmsiIndexElement("RSSHeader_DateAjout_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateNowIndex.addIndex("DateAjout");
		addChamp(MyDateNowIndex);
 
		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);
		
		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSSHeader_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);

		addChamp(new PmsiFilePartElement("NumLot", PmsiStandardDbTypeEnum.NUMERIC, 3, "(\\d{3})"));
		addChamp(new PmsiFilePartElement("StatutEtablissement", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		
		addChamp(new PmsiFilePartElement("DbtPeriode", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		PmsiIndexElement MyDbtPeriodeIndex = new PmsiIndexElement("RSSHeader_DbtPeriode_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDbtPeriodeIndex.addIndex("DbtPeriode");
		addChamp(MyDbtPeriodeIndex);
		
		addChamp(new PmsiFilePartElement("FinPeriode", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		PmsiIndexElement MyFinPeriodeIndex = new PmsiIndexElement("RSSHeader_FinPeriode_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinPeriodeIndex.addIndex("FinPeriode");
		addChamp(MyFinPeriodeIndex);
		
		addChamp(new PmsiFilePartElement("NbEnregistrements", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new PmsiFilePartElement("NbRSS", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new PmsiFilePartElement("PremierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new PmsiFilePartElement("DernierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new PmsiFilePartElement("DernierEnvoiTrimestre", PmsiStandardDbTypeEnum.CHAR, 1, "(.{1})"));
	}
	
}
