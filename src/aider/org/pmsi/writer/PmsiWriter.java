package aider.org.pmsi.writer;

import aider.org.pmsi.parser.exceptions.PmsiIOWriterException;
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
	 * @throws PmsiIOWriterException
	 */
	public void writeStartDocument(String name, String[] attributes, String[] values) throws PmsiIOWriterException;
	
	/**
	 * Crée un élément
	 * @param name nom de l'élément
	 * @throws PmsiIOWriterException
	 */
	public void writeStartElement(String name) throws PmsiIOWriterException;
	
	/**
	 * Clôt l'élément en cours
	 * @throws PmsiIOWriterException
	 */
	public void writeEndElement() throws PmsiIOWriterException;

	/**
	 * Insère un élément avec les données d'une ligne. Attention, l'élément n'est pas fermé
	 * par cette méthode
	 * @param lineType
	 * @throws PmsiIOWriterException
	 */
	public void writeLineElement(PmsiLineType lineType)  throws PmsiIOWriterException;
	
	/**
	 * Cloture l'élément en cours
	 * @throws PmsiIOWriterException
	 */
	public void writeEndDocument() throws PmsiIOWriterException;
	
	/**
	 * Ferme les objets de flux utilisés, libère les resources utilisées
	 * @throws PmsiIOWriterException
	 */
	public void close() throws PmsiIOWriterException;
	
	/**
	 * Renvoie la dernière ligne insérée
	 * @return objet de la dernière ligne insérée
	 * @throws PmsiIOWriterException
	 */
	public PmsiLineType getLastLine() throws PmsiIOWriterException;
}
