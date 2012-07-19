package aider.org.pmsi.dto;

import java.util.HashMap;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Interface d'écriture de xml. A partir de ces 7 méthodes, un fichier pmsi
 * peut être sérialisé. 
 * @author delabre
 *
 */
public interface PmsiPipedWriter {

	/**
	 * Initialise le document et écrit l'entête
	 * @param name nom de la première balise
	 * @param attributes liste des noms des attributs
	 * @param values liste des valeurs des attributs
	 * @throws PmsiPipedIOException
	 */
	public void writeStartDocument(String name, String[] attributes, String[] values) throws PmsiPipedIOException;
	
	/**
	 * Crée un élément
	 * @param name nom de l'élément
	 * @throws PmsiPipedIOException
	 */
	public void writeStartElement(String name) throws PmsiPipedIOException;
	
	/**
	 * Clôt l'élément en cours
	 * @throws PmsiPipedIOException
	 */
	public void writeEndElement() throws PmsiPipedIOException;

	/**
	 * Insère un élément avec les données d'une ligne. Attention, l'élément n'est pas fermé
	 * par cette méthode
	 * @param lineType
	 * @throws PmsiPipedIOException
	 */
	public void writeLineElement(PmsiLineType lineType)  throws PmsiPipedIOException;
	
	/**
	 * Cloture l'élément en cours
	 * @throws PmsiPipedIOException
	 */
	public void writeEndDocument() throws PmsiPipedIOException;
	
	/**
	 * Ferme les objets de flux utilisés, libère les resources utilisées
	 * @throws PmsiPipedIOException
	 */
	public void close() throws PmsiPipedIOException;
	
	/**
	 * Renvoie la dernière ligne insérée
	 * @return objet de la dernière ligne insérée
	 * @throws PmsiPipedIOException
	 */
	public PmsiLineType getLastLine() throws PmsiPipedIOException;
	
	public boolean getStatus();
	
	public HashMap<PmsiDtoReportError, Object> getReport();
}
