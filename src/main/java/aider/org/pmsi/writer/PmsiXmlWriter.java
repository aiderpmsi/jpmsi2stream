package aider.org.pmsi.writer;

import java.io.OutputStream;
import java.io.PipedOutputStream;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Implémente l'interface PmsiWriter
 * Crée un {@link PipedOutputStream} dans lequel sont écrites les données xml
 * pour transformer le flux pmsi en flux xml.
 * @author delabre
 *
 */
public class PmsiXmlWriter implements PmsiWriter {

	/**
	 * Flux xml dans lequel on écrit le fichier final (repose sur {@link PmsiXmlWriter#out}
	 * Il faut faire attention à l'ordre de création et de destruction
	 */
	private XMLStreamWriter xmlWriter = null;
	
	public PmsiXmlWriter() {
		// Ne rien faire
	}
	
	/**
	 * Construction, écrit sur le flux sortant fourni, avec l'encoding désiré
	 * @param outputStream
	 * @param encoding
	 * @throws PmsiWriterException
	 */
	public PmsiXmlWriter(OutputStream outputStream, String encoding) throws PmsiWriterException {
		open(outputStream, encoding);
	}

	/**
	 * Utilise un {@link OutputStream} pour écrire le flux xml
	 * @param outputStream
	 * @param encoding
	 * @throws PmsiWriterException
	 */
	public void open(OutputStream outputStream, String encoding) throws PmsiWriterException {
		// Si le flux xmlwriter est déjà ouvert, il faut lancer une exception
		checkIsClosed();

		try {
			// Création du writer de xml
			xmlWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().
					createXMLStreamWriter(outputStream, encoding));
		} catch (XMLStreamException e) {
			throw new PmsiWriterException(e);
		}
		
	}
	
	/**
	 * Ouvre le document
	 * @param name Nom de la balise initiale
	 * @throws PmsiWriterException 
	 */
	public void writeStartDocument(String name, String[] attributes, String[] values, int lineNumber) throws PmsiWriterException {
		checkIsOpen();
		try {
			xmlWriter.writeStartDocument();
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attributes.length ; i++) {
				xmlWriter.writeAttribute(attributes[i], values[i]);
			}
			xmlWriter.writeAttribute("lineNumber", Integer.toString(lineNumber));
		} catch (Exception e) {
			throw new PmsiWriterException(e);
		}
	}
	
	/**
	 * Ecrit un nouvel élément
	 * @param name nom de l'élément
	 * @throws PmsiWriterException
	 */
	public void writeStartElement(String name, int lineNumber) throws PmsiWriterException {
		checkIsOpen();
		try {
			xmlWriter.writeStartElement(name);
			xmlWriter.writeAttribute("lineNumber", Integer.toString(lineNumber));
		} catch (Exception e) {
			throw new PmsiWriterException(e);
		}
	}

	/**
	 * Ferme l'élément en cours
	 * @throws PmsiWriterException
	 */
	public void writeEndElement(int lineNumber) throws PmsiWriterException {
		checkIsOpen();
		try {
			xmlWriter.writeEndElement();
		} catch (Exception e) {
			throw new PmsiWriterException(e);
		}
	}

	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement
	 * @param lineType
	 * @throws PmsiWriterException
	 */
	public void writeLineElement(PmsiLineType lineType, int lineNumber) throws PmsiWriterException {
		checkIsOpen();
		writeLineElement(lineType.getName(), lineType.getNames(), lineType.getContent(), lineNumber);
	}
	
	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement, il faut le fermer dans un deuxième
	 * temps avec {@link PmsiWriter#writeEndElement()}
	 * @param name Nom de l'élément
	 * @param attNames Nom des attributs
	 * @param attContent Valeur des attributs
	 * @throws PmsiWriterException
	 */
	private void writeLineElement(String name, String[] attNames, String[] attContent, int lineNumber) throws PmsiWriterException {
		try {
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attNames.length ; i++) {
				xmlWriter.writeAttribute(attNames[i], attContent[i]);
			}
			xmlWriter.writeAttribute("lineNumber", Integer.toString(lineNumber));
		} catch (Exception e) {
			throw new PmsiWriterException(e);
		}
	}

	/**
	 * Ecrit la fin du document
	 * @throws PmsiWriterException
	 */
	public void writeEndDocument(int lineNumber) throws PmsiWriterException {
		checkIsOpen();
		try {
	        // Ecriture de la fin du document xml
			xmlWriter.writeEndDocument();

		} catch (XMLStreamException e) {
			throw new PmsiWriterException(e);
		}
	}

	/**
	 * Libère toutes les ressources associées à ce writer
	 * @throws PmsiWriterException
	 */
	public void close() throws PmsiWriterException {
		// Fermeture des flux créés dans cette classe si besoin
		try {
			if (xmlWriter != null) {
				xmlWriter.flush();
				xmlWriter.close();
				xmlWriter = null;
			}

		} catch (XMLStreamException e) {
			throw new PmsiWriterException(e);
		}
	}
	
	/**
	 * Vérifie que le flux est bien ouvert avant chaque écriture dedans
	 * @throws PmsiWriterException 
	 */
	private void checkIsOpen() throws PmsiWriterException {
		if (xmlWriter == null)
			throw new PmsiWriterException("Tentative d'écriture dans un flux fermé");
	}
	
	/**
	 * Vérifie que le flux est bien fermé avant son overture
	 * @throws PmsiWriterException 
	 */
	private void checkIsClosed() throws PmsiWriterException {
		if (xmlWriter != null)
			throw new PmsiWriterException("Tentative d'écriture dans un flux fermé");
	}
	
}
