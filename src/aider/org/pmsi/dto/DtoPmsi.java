package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import ru.ispras.sedna.driver.DriverException;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public abstract class DtoPmsi implements DtoPmsiLineType {

	protected PipedInputStream in = null;
	
	private PrintStream out = null;
	
	protected Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	protected String name;
	
	protected Semaphore sem = new Semaphore(1);
	
	protected XMLStreamWriter xmlWriter = null;
	
	/**
	 * Construction de la connexion à la base de données à partir des configurations
	 * données
	 * @throws DtoPmsiException 
	 */
	public DtoPmsi() throws DtoPmsiException {
		try {
			in = new PipedInputStream();
			out = new PrintStream(new PipedOutputStream(in), false, "UTF-8");
			xmlWriter = XMLOutputFactory.newInstance().
					createXMLStreamWriter(out);
			
			final DtoPmsi dtoRsf = this;
		
			sem.acquire();
			
			new Thread(
				    new Runnable(){
				      public void run() {
				    	  try {
				    		  dtoRsf.storeInputStream();
				    	  } catch (Exception e) {
				    		  if (e instanceof DriverException && ((DriverException) e).getErrorCode() == 168) {
				    			  // Ne rien faire
				    		  } else if (e instanceof DriverException ||e instanceof IOException) {
				    			  e.printStackTrace();
				    			  throw new RuntimeException(e);
				    		  }
				    	  } finally {
				    		  dtoRsf.sem.release();
				    	  }
				      }
				    }
				  ).start();
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}
		
	private void storeInputStream() throws DtoPmsiException {
		try {
			byte buffer[] = new byte[512];
			int size;
			while ((size = in.read(buffer)) != -1) {
				System.out.println(new String(buffer, 0, size));
			}
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		} finally {
			sem.release();
		}
	}
	
	/**
	 * Fermeture de la connexion à la base de données :
	 * Supprime toutes les données qui n'ont pas été validées
	 * et libère toutes les ressources associées à cette connexion
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
			// Wait for the insertion to finish
			sem.acquire();
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}
	
	/**
	 * Ouvre une transaction pour enregistrer le xml et en remplit le début
	 * @param name Nom de la balise initiale
	 * @throws DtoPmsiException 
	 */
	public void writeStartDocument(String name) throws DtoPmsiException {
		try {
			this.name = name;
			
			xmlWriter.writeStartDocument();
			xmlWriter.writeStartElement(name);
			xmlWriter.writeAttribute("insertionTimeStamp", "now");
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}

	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 * @throws DtoPmsiException 
	 */
	public abstract void writeLineElement(PmsiLineType lineType) throws DtoPmsiException;
		
	/**
	 * Clôture de l'enregistrement du fichier dans la base de données
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
			
	        // End the document
			xmlWriter.writeEndDocument();
			xmlWriter.close();
			xmlWriter = null;
			out.close();
			out = null;
			
			// Wait for the insertion to finish
			sem.acquire();
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}

	
	protected void writeSimpleElement(PmsiLineType lineType) throws DtoPmsiException {
			writeSimpleElement(lineType.getName(), lineType.getNames(), lineType.getContent());
	}
	
	protected void writeSimpleElement(String name, String[] attNames, String[] attContent) throws DtoPmsiException {
		try {
			xmlWriter.writeStartElement(name);
			for (int i = 0 ; i < attNames.length ; i++) {
				xmlWriter.writeAttribute(attNames[i], attContent[i]);
			}
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}
}
