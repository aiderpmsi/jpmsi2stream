package aider.org.pmsi.dto;

import ru.ispras.sedna.driver.DriverException;
import aider.org.pmsi.parser.PmsiReader;

/**
 * Classe créant le dto adapté à chaque PmsiReader
 * @author delabre
 *
 */
public class PmsiPipedReaderFactory {
	
	/**
	 * Constructeur par défaut, ne fait rien
	 * @throws DriverException
	 */
	public PmsiPipedReaderFactory() throws PmsiPipedIOException {
	}
	
	/**
	 * Crée un objet de tranfert de données et le renvoie
	 * @param reader le lecteur de pmsi ayant besoin de cet objet
	 * @return L'objet de transfert de donné adapté
	 * @throws PmsiPipedIOException
	 */
	public PmsiPipedReader getPmsiPipedReader(PmsiReader<?, ?> reader) throws PmsiPipedIOException {
		return new PmsiPipedReaderImpl();
	}
	
	/**
	 * Libère les resource associées à cette fabrique
	 */
	public void close() {
	}
}
