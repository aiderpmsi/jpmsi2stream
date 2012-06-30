package aider.org.pmsi.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringEscapeUtils;
import ru.ispras.sedna.driver.DriverException;
import ru.ispras.sedna.driver.SednaConnection;
import ru.ispras.sedna.driver.SednaSerializedResult;
import ru.ispras.sedna.driver.SednaStatement;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public abstract class DtoRsf implements DTOPmsiLineType {

	protected SednaConnection connection;
	
	protected PipedInputStream in;
	
	protected PipedOutputStream pout;
	
	protected PrintStream out;
	
	protected Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	protected String name;
	
	protected ReentrantLock mutex = new ReentrantLock();
	
	protected String datexml;
	
	/**
	 * Construction de la connexion à la base de données à partir des configurations
	 * données
	 * @param dbEnvironment
	 * @param xmlManagerConfig
	 * @param xmlContainerConfig
	 * @throws DriverException 
	 * @throws IOException 
	 * @throws FileNotFoundException
	 * @throws DatabaseException
	 */
	public DtoRsf(SednaConnection connection) throws DriverException, IOException {
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
		
		final DtoRsf dtoRsf = this;

		mutex.lock();
		
		new Thread(
			    new Runnable(){
			      public void run() {
			    	  try {
			    		  dtoRsf.storeInputStream();
			    	  } catch (Exception e) {
			    		  if (e instanceof DriverException || e instanceof IOException) {
			    			  e.printStackTrace();
			    			  try {
			    				  dtoRsf.connection.rollback();
			    			  } catch (DriverException e1) {
			    				  e1.printStackTrace();
			    			  }
			    		  }
			    		  throw new RuntimeException(e);
			    	  } finally {
			    		  dtoRsf.mutex.unlock();
			    	  }
			      }
			    }
			  ).start();
	}
		
	private void storeInputStream() throws DriverException, IOException {
		SednaStatement st = connection.createStatement();
		st.loadDocument(in, null);
	}
	
	/**
	 * Fermeture de la connexion à la base de données :
	 * Supprime toutes les données qui n'ont pas été validées
	 * et libère toutes les ressources associées à cette connexion
	 * @throws DriverException 
	 */
	public void close() throws DriverException {
		
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
	 */
	public void end() {
		// Fermeture de tous les tags non fermés
		while (!lastLine.empty()) {
			out.println("</" + lastLine.pop().getName() + ">");
		}
		out.println("</content>");
		out.println("</" + name + ">");
		
        // End the document
		out.close();
		
		// Wait for the insertion to finish
		mutex.lock();
	
		// Commit the 
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
