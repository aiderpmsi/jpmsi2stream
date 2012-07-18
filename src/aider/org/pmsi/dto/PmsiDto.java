package aider.org.pmsi.dto;

import java.io.InputStream;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Objet de transfert de données entre PmsiPipedReader et l'objet définitif
 * @author delabre
 *
 */
public interface PmsiDto {

	/**
	 * Méthode permettant d'écrire le flux dans l'objet définitif
	 * @param input
	 * @throws PmsiPipedIOException
	 */
	public void writePmsi(InputStream input) throws PmsiPipedIOException;
	
	/**
	 * Libération des ressources de cet objet 
	 * @throws PmsiPipedIOException
	 */
	public void close()  throws PmsiPipedIOException;
}
