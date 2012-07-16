package aider.org.pmsi.dto;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Factory qui crée des objets {@link PmsiPipedReader}. Sa surcharge permet de
 * créer des {@link PmsiPipedReader} différents réalisant un stockage dans d'autres
 * endroits que la sortie standard
 * @author delabre
 *
 */
public class PmsiPipedReaderFactory {
	
	/**
	 * Crée un objet de tranfert de données et le renvoie
	 * @param reader le lecteur de pmsi ayant besoin de cet objet
	 * @return Le {@link PmsiPipedReader} adapté
	 * @throws PmsiPipedIOException
	 */
	public PmsiPipedReader getPmsiPipedReader(PmsiReader<?, ?> reader) throws PmsiPipedIOException {
		return new PmsiPipedReaderImpl();
	}
	
	/**
	 * Libère les ressources associées à cette fabrique
	 */
	public void close() {
	}
}
