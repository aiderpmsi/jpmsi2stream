package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQConstants;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import javax.xml.xquery.XQStaticContext;

import org.apache.commons.lang3.StringEscapeUtils;
import ru.ispras.sedna.driver.DriverException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

public abstract class DtoPmsi implements DTOPmsiLineType {

	protected XQConnection connection;
	
	protected PipedInputStream in = null;
	
	protected PrintStream out = null;
	
	protected Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	protected String name;
	
	protected Semaphore sem = new Semaphore(1);
	
	protected String datexml;
	
	protected String numDocument;
	
	/**
	 * Construction de la connexion à la base de données à partir des configurations
	 * données
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws XQException 
	 * @throws InterruptedException 
	 */
	public DtoPmsi(XQConnection connection) throws UnsupportedEncodingException, IOException, XQException, InterruptedException {
		this.connection = connection;
		
		in = new PipedInputStream();
		out = new PrintStream(new PipedOutputStream(in), false, "UTF-8");

		// Récupération de l'heure d'insertion
		XQPreparedExpression xqpe =
		connection.prepareExpression("current-dateTime()");
		XQResultSequence rs = xqpe.executeQuery();
		rs.next();
		datexml = rs.getItemAsString(null);
		rs.close();
		xqpe.close();
		
		// Récupération du numéro d'insertion après l'avoir incrémenté
		connection.createExpression().executeCommand(
				"update \n" +
				"replace $l in fn:doc(\"PmsiDocIndice\", \"Pmsi\")/indice \n" +
				"with <indice>{$l/text() + 1}</indice>");
		
		xqpe = connection.prepareExpression("fn:doc(\"PmsiDocIndice\", \"Pmsi\")/indice/text()");
		rs = xqpe.executeQuery();
		rs.next();
		numDocument = "pmsi-" + rs.getNode().getTextContent();
		rs.close();
		xqpe.close();

		// Création du document nommé selon numDocument
		connection.createExpression().executeCommand("CREATE DOCUMENT \"" + numDocument + "\" IN COLLECTION \"Pmsi\"");
		
		final DtoPmsi dtoRsf = this;
	
		sem.acquire();
		
		new Thread(
			    new Runnable(){
			      public void run() {
			    	  try {
			    		  dtoRsf.storeInputStream(numDocument);
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
		
	}
		
	private void storeInputStream(String numDocument) throws XQException {
		// Propriété deferred, permet de ne pas lire tout le stream d'un coup
		XQStaticContext properties = connection.getStaticContext();
		properties.setBindingMode(XQConstants.BINDING_MODE_DEFERRED);
		XQExpression xqe = connection.createExpression(properties);
		xqe.bindDocument(new QName("xml"), in, null, null);
		xqe.executeCommand(
				"UPDATE \n" +
				"insert $xml into fn:doc(\"" + numDocument + "\", \"Pmsi\")");
	}
	
	/**
	 * Fermeture de la connexion à la base de données :
	 * Supprime toutes les données qui n'ont pas été validées
	 * et libère toutes les ressources associées à cette connexion
	 * @throws XQException 
	 */
	public void close() throws XQException, InterruptedException {
			// Fermeture du flux si besoin
			if (out != null) { 
				out.close();
				out = null;
				
				// Wait for the insertion to finish
				sem.acquire();
			}
			connection.rollback();
	}
	
	/**
	 * Ouvre une transaction pour enregistrer le xml et en remplit le début
	 * @param name Nom de la balise initiale
	 */
	public void start(String name) {
		this.name = name;
		
		out.println("<" + name + ">");
		out.println("<content insertionTimeStamp=\"" + datexml + "\">");
	}

	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 */
	public abstract void appendContent(PmsiLineType lineType);
		
	/**
	 * Clôture de l'enregistrement du fichier dans la base de données
	 * @throws InterruptedException 
	 * @throws DriverException 
	 */
	public void end() throws InterruptedException, XQException {
		// Fermeture de tous les tags non fermés
		while (!lastLine.empty()) {
			out.println("</" + lastLine.pop().getName() + ">");
		}
		out.println("</content>");
		out.println("</" + name + ">");
		
        // End the document
		out.close();
		out = null;
		
		// Wait for the insertion to finish
		sem.acquire();
	
		// Commit the modifications
		connection.commit();
	}

	
	protected void writeSimpleElement(PmsiLineType lineType) {
		writeSimpleElement(lineType.getName(), lineType.getNames(), lineType.getContent());
	}
	
	protected void writeSimpleElement(String name, String[] attNames, String[] attContent) {
		out.print("<" + name + " ");
		for (int i = 0 ; i < attNames.length ; i++) {
			out.print(attNames[i] + "=\"" + StringEscapeUtils.escapeXml(attContent[i]) + "\" ");
		}
		out.println(">");
	}

}
