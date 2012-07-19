package aider.org.pmsi.dto;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Factory qui crée des objets {@link PmsiThreadedPipedReader}. Sa surcharge permet de
 * créer des {@link PmsiThreadedPipedReader} différents réalisant un stockage dans d'autres
 * endroits que la sortie standard
 * @author delabre
 *
 */
public class PmsiThreadedPipedReaderFactory {
	
	private PmsiDtoFactory pmsiDtoFactory;
	
	public PmsiThreadedPipedReaderFactory(PmsiDtoFactory pmsiDtoFactory) {
		this.pmsiDtoFactory = pmsiDtoFactory;
	}
	
	/**
	 * Crée un objet de tranfert de données et le renvoie
	 * @param reader le lecteur de pmsi ayant besoin de cet objet
	 * @return Le {@link PmsiThreadedPipedReader} adapté
	 * @throws PmsiPipedIOException
	 */
	public PmsiThreadedPipedReader getPmsiPipedReader(PmsiReader<?, ?> reader) throws PmsiPipedIOException {
		PmsiDto pmsiDtoImpl = pmsiDtoFactory.getPmsiDto(reader);
		return new PmsiThreadedPipedReaderImpl(pmsiDtoImpl);
	}
	
	/**
	 * Libère les ressources associées à cette fabrique
	 */
	public void close() {
	}
}
