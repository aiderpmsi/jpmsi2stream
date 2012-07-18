package aider.org.pmsi.dto;

import aider.org.pmsi.parser.PmsiRSF2009Reader;
import aider.org.pmsi.parser.PmsiRSF2012Reader;
import aider.org.pmsi.parser.PmsiRSS116Reader;
import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Classe créant le PipedWriter associé à chaque objet de type {@link PmsiReader}
 * @author delabre
 *
 */
public class PmsiPipedWriterFactory {

	/**
	 * Fabrique d'objets de type {@link PmsiThreadedPipedReader} utilisé dans cette classe
	 */
	private PmsiThreadedPipedReaderFactory pmsiPipedReaderFactory;
	
	/**
	 * Construit à partir de la définition d'une fabrique de {@link PmsiThreadedPipedReader}
	 * l'objet.
	 * @param pmsiPipedReaderFactory : la fabrique qui sera utilisée pour générer les
	 *   {@link PmsiThreadedPipedReader} utilisés par les {@link PmsiPipedWriter}
	 * @throws DriverException
	 */
	public PmsiPipedWriterFactory(PmsiThreadedPipedReaderFactory pmsiPipedReaderFactory) throws PmsiPipedIOException {
		this.pmsiPipedReaderFactory = pmsiPipedReaderFactory;
	}
	
	/**
	 * Crée un {@link PmsiPipedWriter} adapté au reader {@link PmsiReader}
	 * @param reader le lecteur de pmsi ayant besoin de cet objet
	 * @return L'écrivain adapté au type de fichier
	 * @throws PmsiPipedIOException
	 */
	public PmsiPipedWriter getPmsiPipedWriter(PmsiReader<?, ?> reader) throws PmsiPipedIOException {
		PmsiThreadedPipedReader pmsiPipedReader = pmsiPipedReaderFactory.getPmsiPipedReader(reader);
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
	 * Retourne le readerfactory disponible dans cete classe
	 * @return 
	 */
	protected PmsiThreadedPipedReaderFactory getPmsiPipedReaderFactory() {
		return pmsiPipedReaderFactory;
	}
	
	/**
	 * Libère les resource associées à cette fabrique
	 */
	public void close() {
	}
}
