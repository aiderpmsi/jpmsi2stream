package aider.org.pmsi.writer;

import java.io.OutputStream;
import java.util.Stack;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import aider.org.pmsi.parser.exceptions.PmsiIOWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Implémente l'interface PmsiPipedWriter pour transformer le flux pmsi en flux xml
 * @author delabre
 *
 */
public class PmsiWriterImpl implements PmsiWriter {
		
	/**
	 * Flux xml dans lequel on écrit le fichier final (repose sur {@link PmsiWriterImpl#out}
	 * Il faut faire attention à l'ordre de création et de destruction
	 */
	private XMLStreamWriter xmlWriter = null;
	
	/**
	 * Permet de se souvenir quelle ligne a été insérée en dernier
	 */
	private Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	/**
	 * Construction. Associe ce {@link PmsiWriter} au {@link PmsiThreadedPipedReader} en argument
	 * @param pmsiPipedReader
	 * @throws PmsiIOWriterException 
	 */
	public PmsiWriterImpl(OutputStream outputStream, String encoding) throws PmsiIOWriterException {
		try {
			// Création du writer de xml
			xmlWriter = XMLOutputFactory.newInstance().
					createXMLStreamWriter(outputStream, encoding);
		} catch (XMLStreamException e) {
			throw new PmsiIOWriterException(e);
		}
	}

	/**
	 * Ouvre le document
	 * @param name Nom de la balise initiale
	 * @throws PmsiIOWriterException 
	 */
	public void writeStartDocument(String name, String[] attributes, String[] values) throws PmsiIOWriterException {
		try {
			xmlWriter.writeStartDocument();
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attributes.length ; i++) {
				xmlWriter.writeAttribute(attributes[i], values[i]);
			}
		} catch (Exception e) {
			throw new PmsiIOWriterException(e);
		}
	}
	
	/**
	 * Ecrit un nouvel élément
	 * @param name nom de l'élément
	 * @throws PmsiIOWriterException
	 */
	public void writeStartElement(String name) throws PmsiIOWriterException {
		try {
			xmlWriter.writeStartElement(name);
		} catch (Exception e) {
			throw new PmsiIOWriterException(e);
		}
	}

	/**
	 * Ferme l'élément en cours
	 * @throws PmsiIOWriterException
	 */
	public void writeEndElement() throws PmsiIOWriterException {
		try {
			// On prend en compte la fermeture de la ligne (en donc la modification
			// nécessaire de lastLine)
			if (!lastLine.empty())
				lastLine.pop();
			xmlWriter.writeEndElement();
		} catch (Exception e) {
			throw new PmsiIOWriterException(e);
		}
	}

	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement
	 * @param lineType
	 * @throws PmsiIOWriterException
	 */
	public void writeLineElement(PmsiLineType lineType) throws PmsiIOWriterException {
		// On prend en compte l'ajout de ce type de ligne
		lastLine.add(lineType);
		writeLineElement(lineType.getName(), lineType.getNames(), lineType.getContent());
	}
	
	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement, il faut le fermer dans un deuxième
	 * temps avec {@link PmsiWriter#writeEndElement()}
	 * @param name Nom de l'élément
	 * @param attNames Nom des attributs
	 * @param attContent Valeur des attributs
	 * @throws PmsiIOWriterException
	 */
	private void writeLineElement(String name, String[] attNames, String[] attContent) throws PmsiIOWriterException {
		try {
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attNames.length ; i++) {
				xmlWriter.writeAttribute(attNames[i], attContent[i]);
			}
		} catch (Exception e) {
			throw new PmsiIOWriterException(e);
		}
	}

	/**
	 * Ecrit la fin du document
	 * @throws PmsiIOWriterException
	 */
	public void writeEndDocument() throws PmsiIOWriterException {
		try {
			// Fermeture de tous les tags non fermés
			while (!lastLine.empty()) {
				lastLine.pop();
				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();
			
	        // Ecriture de la fin du document xml
			xmlWriter.writeEndDocument();
		} catch (XMLStreamException e) {
			throw new PmsiIOWriterException(e);
		}
	}

	/**
	 * Libère toutes les ressources associées à ce writer
	 * C'est uniquement à ce moment qu'on peut savoir si l'insertion s'est bien déroulée
	 * (c'est le moment où on attend le semaphore du {@link PmsiThreadedPipedReader}
	 * @throws PmsiIOWriterException si l'insertion s'est mal déroulée
	 */
	public void close() throws PmsiIOWriterException {
		// Fermeture des flux créés dans cette classe si besoin
		if (xmlWriter != null) {
			try {
				xmlWriter.close();
				xmlWriter = null;
			} catch (XMLStreamException e) {
				throw new PmsiIOWriterException(e);
			}
		}
	}

	/**
	 * Renvoie la dernière ligne insérée
	 * @return La dernière ligne insérée
	 */
	public PmsiLineType getLastLine() throws PmsiIOWriterException {
		return lastLine.peek();
	}
	
}
