package aider.org.pmsi.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.commons.lang.StringEscapeUtils;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlContainerConfig;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlEventReader;
import com.sleepycat.dbxml.XmlEventWriter;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlQueryExpression;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlValue;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiFileNotReadable;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009m;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009Header;


/**
 * Définition de la lecture d'un RSF version 2009
 * @author delabre
 *
 */
public class PmsiRSF2009Reader extends PmsiReader<PmsiRSF2009Reader.EnumState, PmsiRSF2009Reader.EnumSignal> {

	public enum EnumState {
		STATE_READY, STATE_FINISHED, STATE_EMPTY_FILE,
		WAIT_RSF_HEADER, WAIT_RSF_LINES, WAIT_ENDLINE
	}
	
	public enum EnumSignal {
		SIGNAL_START, // STATE_READY -> WAIT_RSS_HEADER
		SIGNAL_RSF_END_HEADER, // WAIT_RSF_HEADER -> WAIT_RSF_LINES
		SIGNAL_RSF_END_LINES, // WAIT_RSF_LINES -> WAIT_ENDLINE
		SIGNAL_ENDLINE, // WAIT_ENDLINE -> WAIT_RSF_LINES
		SIGNAL_EOF
	}
	
	private static final String name = "RSF2009";
	
	private PmsiLineType lastlineA = null;
	
	/**
	 * Constructeur
	 * @param reader
	 * @throws FileNotFoundException 
	 * @throws DatabaseException 
	 */
	public PmsiRSF2009Reader(Reader reader, OutputStream outStream) throws FileNotFoundException, DatabaseException {
		super(reader, outStream, EnumState.STATE_READY, EnumState.STATE_FINISHED);
	
		// Indication des différents types de ligne que l'on peut rencontrer
		addLineType(EnumState.WAIT_RSF_HEADER, new PmsiRsf2009Header());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2009a());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2009b());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2009c());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2009h());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2009m());

		// Définition des états et des signaux de la machine à états
		addTransition(EnumSignal.SIGNAL_START, EnumState.STATE_READY, EnumState.WAIT_RSF_HEADER);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSF_HEADER, EnumState.STATE_EMPTY_FILE);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSF_LINES, EnumState.STATE_FINISHED);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_ENDLINE, EnumState.STATE_FINISHED);
		addTransition(EnumSignal.SIGNAL_RSF_END_HEADER, EnumState.WAIT_RSF_HEADER, EnumState.WAIT_ENDLINE);
		addTransition(EnumSignal.SIGNAL_RSF_END_LINES, EnumState.WAIT_RSF_LINES, EnumState.WAIT_ENDLINE);
		addTransition(EnumSignal.SIGNAL_ENDLINE, EnumState.WAIT_ENDLINE, EnumState.WAIT_RSF_LINES);

		System.out.println(System.getProperty("user.dir"));
		
		Environment myEnv = null;
		File envHome = new File(System.getProperty("user.dir") + "\\envconf");
		
		EnvironmentConfig envConf = new EnvironmentConfig();
	    envConf.setAllowCreate(true);         // If the environment does not
	                                          // exits, create it.
	    envConf.setInitializeCache(true);     // Turn on the shared memory
	                                          // region.
	    envConf.setInitializeLocking(true);   // Turn on the locking subsystem.
	    envConf.setInitializeLogging(true);   // Turn on the logging subsystem.
	    envConf.setTransactional(true);       // Turn on the transactional 
	                                          // subsystem.

	    myEnv = new Environment(envHome, envConf);
	    
	    XmlManagerConfig managerConfig = new XmlManagerConfig();
	    managerConfig.setAdoptEnvironment(true);
	    XmlManager myManager = new XmlManager(myEnv, managerConfig);
		
	    XmlContainer myContainer = null;
        String theContainer = System.getProperty("user.dir") + "\\container.dbxml";
        
        try {
            myManager = new XmlManager();
            XmlContainerConfig conf = new XmlContainerConfig();
            conf.setAllowCreate(true);
            
           	myContainer = myManager.openContainer(theContainer, conf);

            XmlDocument doc = myManager.createDocument();
            doc.setName("toto");
            
            XmlEventWriter writer = 
            		myContainer.putDocumentAsEventWriter(doc);
            
            writer.writeStartDocument(null, null, null);
            // Write the document's root node. It has no prefixes or
            // attributes. This node is not empty.
            writer.writeStartElement("a", null, null, 0, false);
            writer.writeStartElement("b", null, null, 2, false);
            // Write the "a1" and "b2" attributes on the "b" node
            writer.writeAttribute("a1", null, null, "one", true);
            writer.writeAttribute("b2", null, null, "two", true);
            // Write the "b" node's content. Note that there are 11
            // characters in this text, and we provide that information
            // to the method.
            writer.writeText(XmlEventReader.Characters, "b node text", 11);
            // End the "b" node
            writer.writeEndElement("b", null, null);
            // Start the "c" node. There are no attributes on this node.
            writer.writeStartElement("c", null, null, 0, false);
            // Write the "c" node's content
            writer.writeText(XmlEventReader.Characters, "c node text", 11);
            // End the "c" node and then the "a" (the root) node
            writer.writeEndElement("c", null, null);
            writer.writeEndElement("a", null, null);

            // End the document
            writer.writeEndDocument();
            // Close the document
            writer.close();
            
            XmlQueryContext context = myManager.createQueryContext();
            context.setEvaluationType(XmlQueryContext.Lazy); 
            String myQuery = 
                    "collection('" + System.getProperty("user.dir") + "\\container.dbxml" + "')//*";
            XmlQueryExpression qe = myManager.prepare(myQuery, context);
            
            XmlResults results = qe.execute(context);
            
            while (results.hasNext()) {
            	XmlValue xmlValue = results.next();
            	System.out.println(xmlValue.toString());
            }
            
            results.delete();
            qe.delete();
            
        } catch (XmlException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Stop");
	}
	
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la machine à états
	 * @throws Exception 
	 */
	public void process() throws Exception {
		PmsiLineType matchLine = null;

		switch(getState()) {
		case STATE_READY:
			outStream.write(("<" + name + ">\n").getBytes());
			changeState(EnumSignal.SIGNAL_START);
			readNewLine();
			break;
		case WAIT_RSF_HEADER:
			matchLine = parseLine();
			if (matchLine != null) {
				outStream.write(("<" + matchLine.getName() + ">\n").getBytes());
				for (int i = 0 ; i < matchLine.getContent().length ; i++) {
					outStream.write(("<" + matchLine.getNames()[i] + ">").getBytes());
					outStream.write(StringEscapeUtils.escapeXml(matchLine.getContent()[i]).getBytes());
					outStream.write(("</" + matchLine.getNames()[i] + ">\n").getBytes());
				}
				outStream.write(("</" + matchLine.getName() + ">").getBytes());
				outStream.write("<content>\n".getBytes());
				changeState(EnumSignal.SIGNAL_RSF_END_HEADER);
			} else
				throw new PmsiFileNotReadable("Lecteur RSF : Entête du fichier non trouvée");
			break;
		case WAIT_RSF_LINES:
			matchLine = parseLine();
			if (matchLine != null) {
				if (matchLine instanceof PmsiRsf2009a) {
					if (lastlineA != null)
						outStream.write(("</" + lastlineA.getName() + ">\n").getBytes());
					lastlineA = matchLine;
				}
				outStream.write(("<" + matchLine.getName() + ">\n").getBytes());
				for (int i = 0 ; i < matchLine.getContent().length ; i++) {
					outStream.write(("<" + matchLine.getNames()[i] + ">").getBytes());
					outStream.write(matchLine.getContent()[i].getBytes());
					outStream.write(("</" + matchLine.getNames()[i] + ">\n").getBytes());
				}
				outStream.write(("</" + matchLine.getName() + ">\n").getBytes());
				changeState(EnumSignal.SIGNAL_RSF_END_LINES);
			} else
				throw new PmsiFileNotReadable("Lecteur RSF : Ligne non reconnue");
			break;
		case WAIT_ENDLINE:
			// On vérifie qu'il ne reste rien
			if (getLineSize() != 0)
				throw new PmsiFileNotReadable("trop de caractères dans la ligne");
			changeState(EnumSignal.SIGNAL_ENDLINE);
			readNewLine();
			break;
		case STATE_EMPTY_FILE:
			throw new PmsiFileNotReadable("Lecteur RSF : ", new IOException("Fichier vide"));
		default:
			throw new RuntimeException("Cas non prévu par la machine à états");
		}
	}

	/**
	 * Fonction exécutée lorsque la fin du flux est rencontrée
	 */
	public void endOfFile() throws Exception {
		if (lastlineA != null)
			outStream.write(("</" + lastlineA.getName() + ">\n").getBytes());
		outStream.write("</content>\n".getBytes());
		outStream.write(("</" + name + ">\n").getBytes());
		changeState(EnumSignal.SIGNAL_EOF);		
	}

}
