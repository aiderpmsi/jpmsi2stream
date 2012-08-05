package aider.org.pmsi.parser;

import java.io.IOException;
import java.io.Reader;

import aider.org.pmsi.exceptions.PmsiReaderException;
import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012Header;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012l;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012m;
import aider.org.pmsi.writer.PmsiWriter;

/**
 * Définition de la lecture d'un RSF version 2009
 * @author delabre
 *
 */
public class PmsiRSF2012Reader extends PmsiReader<PmsiRSF2012Reader.EnumState, PmsiRSF2012Reader.EnumSignal> {

	/**
	 * Liste des états de la machine à états
	 * @author delabre
	 *
	 */	public enum EnumState {
		STATE_READY, STATE_FINISHED, STATE_EMPTY_FILE,
		WAIT_RSF_HEADER, WAIT_RSF_LINES, WAIT_ENDLINE
	}
	
	 /**
	  * Liste des signaux
	  * @author delabre
	  *
	  */
	public enum EnumSignal {
		SIGNAL_START, // STATE_READY -> WAIT_RSS_HEADER
		SIGNAL_RSF_END_HEADER, // WAIT_RSF_HEADER -> WAIT_RSF_LINES
		SIGNAL_RSF_END_LINES, // WAIT_RSF_LINES -> WAIT_ENDLINE
		SIGNAL_ENDLINE, // WAIT_ENDLINE -> WAIT_RSF_LINES
		SIGNAL_EOF
	}
	
	/**
	 * Objet de transfert de données
	 */
	private PmsiWriter writer = null;

	/**
	 * Nom identifiant le parseur
	 */
	private static final String name = "RSF2012"; 
	
	/**
	 * Constructeur
	 * @param reader
	 * @throws PmsiWriterException 

	 */
	public PmsiRSF2012Reader(Reader reader, PmsiWriter writer) throws PmsiWriterException {
		super(reader, EnumState.STATE_READY, EnumState.STATE_FINISHED);
	
		// Indication des différents types de ligne que l'on peut rencontrer
		addLineType(EnumState.WAIT_RSF_HEADER, new PmsiRsf2012Header());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2012a());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2012b());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2012c());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2012h());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2012m());
		addLineType(EnumState.WAIT_RSF_LINES, new PmsiRsf2012l());

		// Définition des états et des signaux de la machine à états
		addTransition(EnumSignal.SIGNAL_START, EnumState.STATE_READY, EnumState.WAIT_RSF_HEADER);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSF_HEADER, EnumState.STATE_EMPTY_FILE);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSF_LINES, EnumState.STATE_FINISHED);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_ENDLINE, EnumState.STATE_FINISHED);
		addTransition(EnumSignal.SIGNAL_RSF_END_HEADER, EnumState.WAIT_RSF_HEADER, EnumState.WAIT_ENDLINE);
		addTransition(EnumSignal.SIGNAL_RSF_END_LINES, EnumState.WAIT_RSF_LINES, EnumState.WAIT_ENDLINE);
		addTransition(EnumSignal.SIGNAL_ENDLINE, EnumState.WAIT_ENDLINE, EnumState.WAIT_RSF_LINES);
		
		// Récupération de la classe de transfert en base de données
		this.writer = writer;
	}
	
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la machine à états
	 * @throws PmsiWriterException 
	 * @throws IOException 
	 * @throws PmsiFileNotReadable 
	 * @throws Exception 
	 */
	public void process() throws PmsiWriterException, PmsiReaderException {
		PmsiLineType matchLine = null;

		switch(getState()) {
		case STATE_READY:
			writer.writeStartDocument(name, new String[0], new String[0]);
			changeState(EnumSignal.SIGNAL_START);
			readNewLine();
			break;
		case WAIT_RSF_HEADER:
			matchLine = parseLine();
			if (matchLine != null) {
				writer.writeLineElement(matchLine);
				changeState(EnumSignal.SIGNAL_RSF_END_HEADER);
			} else {
				throw (PmsiReaderException) new PmsiReaderException("Entête du fichier non trouvée").
					setXmlMessage("<rsf v=\"2012\"><parsingerror>Entête du fichier non trouvée</parsingerror></rsf>");
			}
			break;
		case WAIT_RSF_LINES:
			matchLine = parseLine();
			if (matchLine != null) {
				writer.writeLineElement(matchLine);
				changeState(EnumSignal.SIGNAL_RSF_END_LINES);
			} else {
				throw (PmsiReaderException) new PmsiReaderException("Ligne non reconnue").
					setXmlMessage("<rsf v=\"2012\"><parsingerror>Ligne non reconnue</parsingerror></rsf>");
			}
			break;
		case WAIT_ENDLINE:
			// On vérifie qu'il ne reste rien
			if (getLineSize() != 0)
				throw (PmsiReaderException) new PmsiReaderException("trop de caractères dans la ligne").
					setXmlMessage("<rsf v=\"2012\"><parsingerror>trop de caractères dans la ligne</parsingerror></rsf>");
			changeState(EnumSignal.SIGNAL_ENDLINE);
			readNewLine();
			break;
		case STATE_EMPTY_FILE:
			throw (PmsiReaderException) new PmsiReaderException("Fichier vide").
				setXmlMessage("<rsf v=\"2012\"><parsingerror>Fichier vide</parsingerror></rsf>");
		default:
			throw new RuntimeException("Cas non prévu par la machine à états");
		}
	}

	@Override
	public void endOfFile() throws PmsiReaderException {
		changeState(EnumSignal.SIGNAL_EOF);		
	}

	@Override
	public void finish() throws Exception {
		writer.writeEndDocument();
	}

	@Override
	public void close() throws PmsiWriterException {
		writer.close();
	}

}
