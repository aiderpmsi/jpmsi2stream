package aider.org.pmsi.dto;

import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Stack;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Implémente l'interface DtoPmsi pour transformer le flux pmsi en flux xml
 * @author delabre
 *
 */
public abstract class PmsiPipedWriterImpl implements PmsiPipedWriter {
	
	/**
	 * Classe définissant un inputreader et un thread pour en faire ce qu'il faut
	 * avec comme sortie l'entrée de ce que l'on écrit dans cette classe
	 */
	private PmsiPipedReader inPipedReader;
	
	/**
	 * {@link OutputStream} dans lequel on lit pendant qu'un autre thread écrit
	 * dans l'inpustream
	 */
	private PrintStream out = null;
	
	/**
	 * Flux xml dans lequel on écrit le fichier final (repose sur {@link PmsiPipedWriterImpl#out}
	 * Il faut faire attention à l'ordre de création et de destruction
	 */
	private XMLStreamWriter xmlWriter = null;
	
	/**
	 * Permet de se souvenir quelle ligne a été insérée en dernier
	 */
	private Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	/**
	 * Construction.
	 * @throws PmsiPipedIOException 
	 */
	public PmsiPipedWriterImpl(PmsiPipedReader inPipedReader) throws PmsiPipedIOException {
		try {
			this.inPipedReader = inPipedReader;
			// Création des readers et des writers
			out = new PrintStream(new PipedOutputStream(
					inPipedReader.getPipedInputStream()), false, "UTF-8");
			xmlWriter = XMLOutputFactory.newInstance().
					createXMLStreamWriter(out);
			// Lancement du lecteur
			inPipedReader.start();
		} catch (Exception e) {
			throw new PmsiPipedIOException(e);
		}
	}

	/**
	 * Ouvre le document
	 * @param name Nom de la balise initiale
	 * @throws PmsiPipedIOException 
	 */
	public void writeStartDocument(String name, String[] attributes, String[] values) throws PmsiPipedIOException {
		try {
			xmlWriter.writeStartDocument();
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attributes.length ; i++) {
				xmlWriter.writeAttribute(attributes[i], values[i]);
			}
		} catch (Exception e) {
			throw new PmsiPipedIOException(e);
		}
	}
	
	/**
	 * Ecrit un nouvel élément
	 * @param name nom de l'élément
	 * @throws PmsiPipedIOException
	 */
	public void writeStartElement(String name) throws PmsiPipedIOException {
		try {
			xmlWriter.writeStartElement(name);
		} catch (Exception e) {
			throw new PmsiPipedIOException(e);
		}
	}

	/**
	 * Ferme l'élément en cours
	 * @throws PmsiPipedIOException
	 */
	public void writeEndElement() throws PmsiPipedIOException {
		try {
			// On prend en compte la fermeture de la ligne (en donc la modification
			// nécessaire de lastLine)
			if (!lastLine.empty())
				lastLine.pop();
			xmlWriter.writeEndElement();
		} catch (Exception e) {
			throw new PmsiPipedIOException(e);
		}
	}

	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement
	 * @param lineType
	 * @throws PmsiPipedIOException
	 */
	public void writeLineElement(PmsiLineType lineType) throws PmsiPipedIOException {
		// On prend en compte l'ajout de ce type de ligne
		lastLine.add(lineType);
		writeLineElement(lineType.getName(), lineType.getNames(), lineType.getContent());
	}
	
	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement
	 * @param lineType
	 * @throws PmsiPipedIOException
	 */
	private void writeLineElement(String name, String[] attNames, String[] attContent) throws PmsiPipedIOException {
		try {
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attNames.length ; i++) {
				xmlWriter.writeAttribute(attNames[i], attContent[i]);
			}
		} catch (Exception e) {
			throw new PmsiPipedIOException(e);
		}
	}

	/**
	 * Ecrit la fin du document
	 * @throws PmsiPipedIOException
	 */
	public void writeEndDocument() throws PmsiPipedIOException {
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
			throw new PmsiPipedIOException(e);
		}
	}

	/**
	 * Libère toutes les ressources associées à ce writer
	 * C'est uniquement à ce moment qu'on peut savoir si l'insertion s'est bien déroulée
	 * @throws PmsiPipedIOException si l'insertion s'est mal déroulée
	 */
	public void close() throws PmsiPipedIOException {
		try {
			// Fermeture des flux si besoin
			if (xmlWriter != null) {
				xmlWriter.close();
				xmlWriter = null;
			}
			if (out != null) { 
				out.close();
				out = null;
			}
			
			// On attend que PmsiPipedReader ait fini son boulot
			inPipedReader.getSemaphore().acquire();
			
			// On regarde si l'insertion des données a bien fonctionné
			if (inPipedReader.getStatus() == false)
				throw new PmsiPipedIOException("ecriture impossible", inPipedReader.getTerminalException());
		} catch (Exception e) {
			throw new PmsiPipedIOException(e);
		} finally {
			inPipedReader.close();
		}
	}

	/**
	 * Renvoie la dernière ligne insérée
	 * @return La dernière ligne insérée
	 */
	public PmsiLineType getLastLine() throws PmsiPipedIOException {
		return lastLine.peek();
	}
}
