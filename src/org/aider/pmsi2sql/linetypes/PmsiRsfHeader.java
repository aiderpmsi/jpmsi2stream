package org.aider.pmsi2sql.linetypes;

import org.aider.pmsi2sql.dbtypes.PmsiStandardDbTypeEnum;
import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;

import aider.org.pmsi.parser.PmsiLineType;

/**
 * Capture l'ent�te d'un fichier pmsi RSS
 * @author delabre
 *
 */
public class PmsiRsfHeader extends PmsiLineType {

	/**
	 * Constructeur
	 */
	public PmsiRsfHeader() {
		super("RSFHeader");
				
		PmsiInternalElement MyIdHeader = new PmsiInternalElement("rsfheaderid", PmsiStandardDbTypeEnum.BIGSERIAL, 0);
		MyIdHeader.setValue("nextval('rsfheader_rsfheaderid_seq')");
		addChamp(MyIdHeader);

		PmsiIndexElement MyIdheaderIndex = new PmsiIndexElement("RSFHeader_rsfheaderid_pidx", PmsiIndexElement.INDEX_PK);
		MyIdheaderIndex.addIndex("rsfheaderid");
		addChamp(MyIdheaderIndex);

		PmsiInternalElement MyIdInsertion = new PmsiInternalElement("pmsiinsertionid", PmsiStandardDbTypeEnum.BIGINT, 0);
		MyIdInsertion.setValue("currval('pmsiinsertion_pmsiinsertionid_seq')");
		addChamp(MyIdInsertion);
		PmsiFkElement MyIdInsertionFK = new PmsiFkElement("RSFHeader_pmsiinsertionid_fk", "pmsiinsertion", "DEFERRABLE INITIALLY DEFERRED");
		MyIdInsertionFK.addForeignChamp("pmsiinsertionid", "pmsiinsertionid");
		addChamp(MyIdInsertionFK);
		
		PmsiInternalElement MyLineCounter = new PmsiInternalElement("Line", PmsiStandardDbTypeEnum.INT, 0);
		MyLineCounter.setValue("currval('line_counter')");
		addChamp(MyLineCounter);

		addChamp(new PmsiFilePartElement("FINESS", PmsiStandardDbTypeEnum.NUMERIC, 9, "(\\d{9})"));
		PmsiIndexElement MyFinessIndex = new PmsiIndexElement("RSFHeader_FINESS_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyFinessIndex.addIndex("FINESS");
		addChamp(MyFinessIndex);
		
		addChamp(new PmsiFilePartElement("NumLot", PmsiStandardDbTypeEnum.NUMERIC, 3, "(\\d{3})"));
		addChamp(new PmsiFilePartElement("StatutJuridique", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		addChamp(new PmsiFilePartElement("ModeTarifs", PmsiStandardDbTypeEnum.VARCHAR, 2, "(.{2})"));
		
		addChamp(new PmsiFilePartElement("DateDebut", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		PmsiIndexElement MyDateDebutIndex = new PmsiIndexElement("RSFHeader_DateDebut_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateDebutIndex.addIndex("DateDebut");
		addChamp(MyDateDebutIndex);
		
		addChamp(new PmsiFilePartElement("DateFin", PmsiStandardDbTypeEnum.DATE, 0, "(\\d{8})"));
		PmsiIndexElement MyDateFinIndex = new PmsiIndexElement("RSFHeader_DateFin_idx", PmsiIndexElement.INDEX_SIMPLE);
		MyDateFinIndex.addIndex("DateFin");
		addChamp(MyDateFinIndex);
		
		addChamp(new PmsiFilePartElement("NbEnregistrements", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new PmsiFilePartElement("NbRSS", PmsiStandardDbTypeEnum.NUMERIC, 6, "(\\d{6})"));
		addChamp(new PmsiFilePartElement("PremierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new PmsiFilePartElement("DernierRSS", PmsiStandardDbTypeEnum.NUMERIC, 7, "(\\d{7})"));
		addChamp(new PmsiFilePartElement("DernierEnvoi", PmsiStandardDbTypeEnum.CHAR, 7, "(.{1})"));
		
		
	}
	
}
