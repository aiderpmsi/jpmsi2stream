package aider.org.pmsi.dto;

import java.io.InputStream;

import aider.org.pmsi.exceptions.PmsiDtoException;

/**
 * Interface à implémenter pour définir sa propre classe de transfert de données.
 * Une classe possédant cette interface est à fournir à la construction d'une classe
 * de type {@link PmsiCallable}.
 * <br/>
 * La méthode {@link PmsiDto#transfer()} est appelée pour réaliser le transfert
 * entre le flux de ce fichier pmsi transformé en xml et son container final
 * flux xml
 * <br/>
 * @author delabre
 *
 */
public interface PmsiDto {

	/**
	 * Réalise le travail de transfert entre {@link InputStream} du {@link InOutStream}
	 * et container final de ce fichier pmsi transformé
	 * @throws PmsiDtoException
	 */
	public void transfer() throws PmsiDtoException;
	
	/**
	 * Libère les ressources créées dans cette classe
	 * @throws PmsiDtoException
	 */
	public void close() throws PmsiDtoException;
}
