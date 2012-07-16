package aider.org.pmsi.dto;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Implémente l'interface DtoPmsi pour transformer le flux pmsi en flux xml
 * @author delabre
 *
 */
public abstract class DtoPmsiImpl implements DtoPmsi {
	
	/**
	 * {@link InputStream} qui récupère l'outputstream pour pouvoir avoir un
	 * thread qui écrit dans l'out tandis que l'autre lit dans l'in
	 */
	private PipedInputStream in = null;
	
	/**
	 * {@link OutputStream} dans lequel on lit pendant qu'un autre thread écrit
	 * dans l'inpustream
	 */
	private PrintStream out = null;
	
	/**
	 * Permet de se souvenir quelle ligne a été insérée en dernier
	 */
	private Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	/**
	 * {@link Semaphore} pour synchroniser les deux threads de lecture et écriture :
	 * La lecture attend que l'écriture ait bien finie.  Il est partagé
	 * entre {@link DtoPmsiImpl} et {@link DtoPmsiWriter}
	 */
	private Semaphore sem = new Semaphore(1);
	
	/**
	 * Flux xml dans lequel on écrit le fichier final (repose sur {@link DtoPmsiImpl#out}
	 * Il faut faire attention à l'ordre de création et de destructino
	 */
	private XMLStreamWriter xmlWriter = null;
	
	/**
	 * Thread qui écrira les données xml à partir d'un inputstream dans un container
	 */
	protected DtoPmsiWriter threadWriter;
	
	/**
	 * Construction.
	 * @throws DtoPmsiException 
	 */
	public DtoPmsiImpl() throws DtoPmsiException {
		try {
			// Création des readers et des writers
			in = new PipedInputStream();
			out = new PrintStream(new PipedOutputStream(in), false, "UTF-8");
			xmlWriter = XMLOutputFactory.newInstance().
					createXMLStreamWriter(out);

			// Création du thread d'écriture des données issues de cette classe
			createThreadWriter();
			threadWriter.setSemaphore(sem);
			threadWriter.setInputStream(in);
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}
	
	/**
	 * Méthode permettant de lancer le thread lisant les données que la classe
	 * principale écrit pour les rediriger là où il faut.
	 * @throws DtoPmsiException
	 */
	private void createThreadWriter() throws DtoPmsiException {
		threadWriter = new DtoPmsiWriter();
	}
	
	/**
	 * Ouvre le document
	 * @param name Nom de la balise initiale
	 * @throws DtoPmsiException 
	 */
	public void writeStartDocument(String name) throws DtoPmsiException {
		try {
			xmlWriter.writeStartDocument();
			xmlWriter.writeStartElement(name);
			xmlWriter.writeAttribute("insertionTimeStamp", "now");
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}
	
	/**
	 * Ecrit un nouvel élément
	 * @param name nom de l'élément
	 * @throws DtoPmsiException
	 */
	public void writeStartElement(String name) throws DtoPmsiException {
		try {
			xmlWriter.writeStartElement(name);
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}

	/**
	 * Ferme l'élément en cours
	 * @throws DtoPmsiException
	 */
	public void writeEndElement() throws DtoPmsiException {
		try {
			// On prend en compte la fermeture de la ligne (en donc la modification
			// nécessaire de lastLine)
			if (!lastLine.empty())
				lastLine.pop();
			xmlWriter.writeEndElement();
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}

	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement
	 * @param lineType
	 * @throws DtoPmsiException
	 */
	public void writeLineElement(PmsiLineType lineType) throws DtoPmsiException {
		// On prend en compte l'ajout de ce type de ligne
		lastLine.add(lineType);
		writeLineElement(lineType.getName(), lineType.getNames(), lineType.getContent());
	}
	
	/**
	 * Ouvre un élément en écrivant les attributs associés à la ligne pmsi dedans.
	 * Attention, il n'est pas fermé automatiquement
	 * @param lineType
	 * @throws DtoPmsiException
	 */
	private void writeLineElement(String name, String[] attNames, String[] attContent) throws DtoPmsiException {
		try {
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attNames.length ; i++) {
				xmlWriter.writeAttribute(attNames[i], attContent[i]);
			}
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}

	/**
	 * Ecrit la fin du document
	 * @throws DtoPmsiException
	 */
	public void writeEndDocument() throws DtoPmsiException {
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
			throw new DtoPmsiException(e);
		}
	}

	/**
	 * Libère toutes les ressources associées à ce dto
	 * @throws DtoPmsiException 
	 */
	public void close() throws DtoPmsiException{
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
			
			// On attend que DtoPmsiWriter ait fini son boulot
			sem.acquire();
			
			// On regarde si l'insertion des données a bien fonctionné
			if (threadWriter.getStatus() == false)
				throw new Exception(threadWriter.getException());
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}

	/**
	 * Renvoie la dernière ligne insérée
	 * @return La dernière ligne insérée
	 */
	public PmsiLineType getLastLine() throws DtoPmsiException {
		return lastLine.peek();
	}
}
