package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.StringEscapeUtils;
import ru.ispras.sedna.driver.DriverException;
import ru.ispras.sedna.driver.SednaConnection;
import ru.ispras.sedna.driver.SednaSerializedResult;
import ru.ispras.sedna.driver.SednaStatement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public abstract class DtoPmsi implements DTOPmsiLineType {

	protected SednaConnection connection;
	
	protected PipedInputStream in;
	
	protected PipedOutputStream pout;
	
	protected PrintStream out;
	
	protected Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	protected String name;
	
	protected Semaphore sem = new Semaphore(1);
	
	protected String datexml;
	
	protected String numDocument;
	
	/**
	 * Construction de la connexion à la base de données à partir des configurations
	 * données
	 * @throws DriverException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public DtoPmsi(SednaConnection connection) throws DriverException, IOException, InterruptedException {
		this.connection = connection;
		connection.begin();
		
		in = new PipedInputStream();
		pout = new PipedOutputStream(in);
		out = new PrintStream(pout, false, "UTF-8");

		// Récupération de l'heure d'insertion
		SednaStatement st = connection.createStatement();
		st.execute("current-dateTime()");
		SednaSerializedResult pr = st.getSerializedResult();
		datexml = pr.next();
		
		// Récupération du numéro d'insertion
		st = connection.createStatement();
		st.execute("update \n" +
				"replace $l in fn:doc(\"PmsiDocIndice\", \"Pmsi\")/indice \n" +
				"with <indice>{$l/text() + 1}</indice>");
		
		st = connection.createStatement();
		st.execute("fn:doc(\"PmsiDocIndice\", \"Pmsi\")/indice/text()");
		pr = st.getSerializedResult();
		numDocument = pr.next();
		
		final DtoPmsi dtoRsf = this;
	
		sem.acquire();
		
		new Thread(
			    new Runnable(){
			      public void run() {
			    	  try {
			    		  dtoRsf.storeInputStream();
			    	  } catch (Exception e) {
			    		  if (e instanceof DriverException || e instanceof IOException) {
			    			  e.printStackTrace();
			    		  }
			    		  throw new RuntimeException(e);
			    	  } finally {
			    		  dtoRsf.sem.release();
			    	  }
			      }
			    }
			  ).start();
		
	}
		
	private void storeInputStream() throws DriverException, IOException {
		SednaStatement st = connection.createStatement();
		st.loadDocument(in, "pmsi-" + numDocument, "Pmsi");
	}
	
	/**
	 * Fermeture de la connexion à la base de données :
	 * Supprime toutes les données qui n'ont pas été validées
	 * et libère toutes les ressources associées à cette connexion
	 * @throws DriverException 
	 */
	public void close() throws DriverException {
		try {
			connection.rollback();
		} catch (DriverException e) {
			if (e.getErrorCode() == 411) {
				// Do nothing
			} else
				throw e;
		}
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
	public void end() throws InterruptedException, DriverException {
		// Fermeture de tous les tags non fermés
		while (!lastLine.empty()) {
			out.println("</" + lastLine.pop().getName() + ">");
		}
		out.println("</content>");
		out.println("</" + name + ">");
		
        // End the document
		out.close();
		
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
