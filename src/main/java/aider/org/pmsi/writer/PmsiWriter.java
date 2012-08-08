package aider.org.pmsi.writer;

import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Interface d'écriture de xml. A partir de ces 7 méthodes, un fichier pmsi
 * peut être sérialisé. 
 * @author delabre
 *
 */
public interface PmsiWriter {

	/**
	 * Initialise le document et écrit l'entête
	 * @param name nom de la première balise
	 * @param attributes liste des noms des attributs
	 * @param values liste des valeurs des attributs
	 * @throws PmsiWriterException
	 */
	public void writeStartDocument(String name, String[] attributes, String[] values) throws PmsiWriterException;
	
	/**
	 * Crée un élément
	 * @param name nom de l'élément
	 * @throws PmsiWriterException
	 */
	public void writeStartElement(String name) throws PmsiWriterException;
	
	/**
	 * Clôt l'élément en cours
	 * @throws PmsiWriterException
	 */
	public void writeEndElement() throws PmsiWriterException;

	/**
	 * Insère un élément avec les données d'une ligne. Attention, l'élément n'est pas fermé
	 * par cette méthode
	 * @param lineType
	 * @throws PmsiWriterException
	 */
	public void writeLineElement(PmsiLineType lineType)  throws PmsiWriterException;
	
	/**
	 * Cloture l'élément en cours
	 * @throws PmsiWriterException
	 */
	public void writeEndDocument() throws PmsiWriterException;
	
	/**
	 * Ferme les objets de flux utilisés, libère les resources utilisées
	 * @throws PmsiWriterException
	 */
	public void close() throws PmsiWriterException;
}
