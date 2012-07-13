package aider.org.pmsi.reader;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.xml.xquery.XQException;

import aider.org.pmsi.dto.DTOPmsiLineType;
import aider.org.pmsi.dto.DTOPmsiReaderFactory;
import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiFileNotReadable;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012Header;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012l;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012m;

/**
 * Définition de la lecture d'un RSF version 2009
 * @author delabre
 *
 */
public class PmsiRSF2012Reader extends PmsiReader<PmsiRSF2012Reader.EnumState, PmsiRSF2012Reader.EnumSignal> {

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
	
	private DTOPmsiLineType dtoPmsiLineType = null;

	private static final String name = "RSF2012"; 
	
	/**
	 * Constructeur
	 * @param reader
	 * @throws InterruptedException 
	 * @throws XQException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public PmsiRSF2012Reader(Reader reader, DTOPmsiReaderFactory dtoPmsiReaderFactory) throws UnsupportedEncodingException, IOException, XQException, InterruptedException {
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
		dtoPmsiLineType = dtoPmsiReaderFactory.getDtoPmsiLineType(this);
	}
	
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la machine à états
	 * @throws Exception 
	 */
	public void process() throws Exception {
		PmsiLineType matchLine = null;

		switch(getState()) {
		case STATE_READY:
			dtoPmsiLineType.start(name);
			changeState(EnumSignal.SIGNAL_START);
			readNewLine();
			break;
		case WAIT_RSF_HEADER:
			matchLine = parseLine();
			if (matchLine != null) {
				dtoPmsiLineType.appendContent(matchLine);
				changeState(EnumSignal.SIGNAL_RSF_END_HEADER);
			} else
				throw new PmsiFileNotReadable("Lecteur RSF : Entête du fichier non trouvée");
			break;
		case WAIT_RSF_LINES:
			matchLine = parseLine();
			if (matchLine != null) {
				dtoPmsiLineType.appendContent(matchLine);
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
		changeState(EnumSignal.SIGNAL_EOF);		
	}

	public void finish() throws Exception {
		dtoPmsiLineType.end();
	}

	@Override
	public void close() throws Exception {
		dtoPmsiLineType.close();
	}
	
}
