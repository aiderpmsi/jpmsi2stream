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
public class PmsiPipedWriterFactory {
	
	private PmsiPipedReaderFactory pmsiPipedReaderFactory;
	
	/**
	 * Constructeur par défaut, ne fait rien
	 * @throws DriverException
	 */
	public PmsiPipedWriterFactory(PmsiPipedReaderFactory pmsiPipedReaderFactory) throws PmsiPipedIOException {
		this.pmsiPipedReaderFactory = pmsiPipedReaderFactory;
	}
	
	/**
	 * Crée un objet de tranfert de données et le renvoie
	 * @param reader le lecteur de pmsi ayant besoin de cet objet
	 * @return L'objet de transfert de donné adapté
	 * @throws PmsiPipedIOException
	 */
	public PmsiPipedWriter getDtoPmsiLineType(PmsiReader<?, ?> reader) throws PmsiPipedIOException {
		PmsiPipedReader pmsiPipedReader = pmsiPipedReaderFactory.getPmsiPipedReader(reader);
		if (reader instanceof PmsiRSF2009Reader) {
			return new Rsf2009PipedWriter(pmsiPipedReader);
		} else if (reader instanceof PmsiRSF2012Reader) {
			return new Rsf2012PipedWriter(pmsiPipedReader);
		} else if (reader instanceof PmsiRSS116Reader) {
			return new Rss116PipedWriter(pmsiPipedReader);
		}
		return null;
	}
	
	/**
	 * Libère les resource associées à cette fabrique
	 */
	public void close() {
	}
}
