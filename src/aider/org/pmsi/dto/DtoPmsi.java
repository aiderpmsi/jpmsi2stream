package aider.org.pmsi.dto;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Interface de gestion de transfert de données.
 * A partir de ces 7 méthodes de classe, un fichier pmsi peut être sérialisé
 * @author delabre
 *
 */
public interface DtoPmsi {

	/**
	 * Initialise le document et écrit l'entête
	 * @param name nom de la première balise
	 * @throws DtoPmsiException
	 */
	public void writeStartDocument(String name) throws DtoPmsiException;
	
	/**
	 * Crée un élément
	 * @param name nom de l'élément
	 * @throws DtoPmsiException
	 */
	public void writeStartElement(String name) throws DtoPmsiException;
	
	/**
	 * Clôt l'élément en cours
	 * @throws DtoPmsiException
	 */
	public void writeEndElement() throws DtoPmsiException;

	/**
	 * Insère un élément avec les données d'une ligne. Attention, l'élément n'est pas fermé
	 * par cette méthode
	 * @param lineType
	 * @throws DtoPmsiException
	 */
	public void writeLineElement(PmsiLineType lineType)  throws DtoPmsiException;
	
	/**
	 * Cloture l'élément en cours
	 * @throws DtoPmsiException
	 */
	public void writeEndDocument() throws DtoPmsiException;
	
	/**
	 * Ferme les objets de flux utilisés, libère les resources utilisées
	 * @throws DtoPmsiException
	 */
	public void close() throws DtoPmsiException;
	
	/**
	 * Renvoie la dernière ligne insérée
	 * @return
	 * @throws DtoPmsiException
	 */
	public PmsiLineType getLastLine() throws DtoPmsiException;
}
