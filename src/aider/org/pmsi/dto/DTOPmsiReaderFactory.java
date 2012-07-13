package aider.org.pmsi.dto;

import ru.ispras.sedna.driver.DriverException;
import aider.org.pmsi.parser.PmsiRSF2009Reader;
import aider.org.pmsi.parser.PmsiRSF2012Reader;
import aider.org.pmsi.parser.PmsiRSS116Reader;
import aider.org.pmsi.parser.PmsiReader;

/**
 * Classe créant le dto adapté à chaque PmsiReader
 * @author delabre
 *
 */
public class DTOPmsiReaderFactory {
	
	/**
	 * Constructeur par défaut, ne fait rien
	 * @throws DriverException
	 */
	public DTOPmsiReaderFactory() throws DriverException {
	}
	
	/**
	 * Crée un objet de tranfert de données et le renvoie
	 * @param reader le lecteur de pmsi ayant besoin de cet objet
	 * @return L'objet de transfert de donné adapté
	 * @throws DtoPmsiException
	 */
	public DtoPmsi getDtoPmsiLineType(PmsiReader<?, ?> reader) throws DtoPmsiException {
		if (reader instanceof PmsiRSF2009Reader) {
			return new DtoRsf2009();
		} else if (reader instanceof PmsiRSF2012Reader) {
			return new DtoRsf2012();
		} else if (reader instanceof PmsiRSS116Reader) {
			return new DtoRss116();
		}
		return null;
	}
	
	/**
	 * Libère les resource associées à cette fabrique
	 */
	public void close() {
	}
}
