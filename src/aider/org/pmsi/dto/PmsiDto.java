package aider.org.pmsi.dto;

import java.io.InputStream;
import java.util.HashMap;

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
	 * Permet de récupérer des informations sur la manière dont les données ont été
	 * bien insérées ou pas
	 * @return Une chaine de caractère explicative
	 */
	public HashMap<PmsiDtoReportError, Object> getReport();
	
	/**
	 * Retourne l'état d'insertion de PmsiDto (avec la compilation de
	 * PmsiDtoReport)
	 * @return
	 */
	public boolean getStatus();
	
	/**
	 * Libération des ressources de cet objet 
	 * @throws PmsiPipedIOException
	 */
	public void close()  throws PmsiPipedIOException;
}
