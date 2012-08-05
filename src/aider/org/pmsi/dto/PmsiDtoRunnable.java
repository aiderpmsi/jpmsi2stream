package aider.org.pmsi.dto;

import java.io.InputStream;

import aider.org.pmsi.exceptions.PmsiDtoRunnableException;

/**
 * Interface à implémenter pour définir sa propre classe de transfert de données.
 * Une classe possédant cette interface est à fournir à la construction d'une classe
 * de type {@link PmsiThread}.
 * <br/>
 * La méthode {@link PmsiDtoRunnable#run()} est appelée pour réaliser le transfert
 * entre le flux de ce fichier pmsi transformé en xml et son container final
 * flux xml
 * <br/>
 * @author delabre
 *
 */
public interface PmsiDtoRunnable {

	/**
	 * Réalise le travail de transfert entre {@link InputStream} du {@link PmsiStreamMuxer}
	 * et container final de ce fichier pmsi transformé
	 * @throws PmsiDtoRunnableException
	 */
	public void run() throws PmsiDtoRunnableException;
	
	/**
	 * Libère les ressources créées dans cette classe
	 * @throws PmsiDtoRunnableException
	 */
	public void close() throws PmsiDtoRunnableException;
}
