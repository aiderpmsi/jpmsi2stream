package aider.org.pmsi.parser;

import java.io.IOException;
import java.io.Reader;

import aider.org.pmsi.dto.PmsiPipedWriter;
import aider.org.pmsi.dto.PmsiPipedWriterFactory;
import aider.org.pmsi.parser.exceptions.PmsiIOException;
import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;
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

	/**
	 * Liste des états de la machine à états
	 * @author delabre
	 *
	 */
	public enum EnumState {
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
	 * Nom identifiant cette classe de PmsiReader
	 */
	private static final String name = "RSF2009";
	
	/**
	 * Objet de transfert de données
	 */
	private PmsiPipedWriter pmsiPipedWriter = null;
	
	/**
	 * Constructeur
	 * @param reader
	 * @throws PmsiPipedIOException 
	 */
	public PmsiRSF2009Reader(Reader reader, PmsiPipedWriterFactory dtoPmsiReaderFactory) throws PmsiPipedIOException {
		super(reader, EnumState.STATE_READY, EnumState.STATE_FINISHED);
	
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

		// Récupération de la classe de transfert en base de données
		pmsiPipedWriter = dtoPmsiReaderFactory.getDtoPmsiLineType(this);
	}
	
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la machine à états
	 * @throws PmsiPipedIOException 
	 * @throws IOException 
	 * @throws PmsiFileNotReadable 
	 */
	public void process() throws PmsiPipedIOException, PmsiIOException {
		PmsiLineType matchLine = null;

		switch(getState()) {
		case STATE_READY:
			pmsiPipedWriter.writeStartDocument(name, new String[0], new String[0]);
			changeState(EnumSignal.SIGNAL_START);
			readNewLine();
			break;
		case WAIT_RSF_HEADER:
			matchLine = parseLine();
			if (matchLine != null) {
				pmsiPipedWriter.writeLineElement(matchLine);
				changeState(EnumSignal.SIGNAL_RSF_END_HEADER);
			} else
				throw new PmsiIOException("Lecteur RSF : Entête du fichier non trouvée");
			break;
		case WAIT_RSF_LINES:
			matchLine = parseLine();
			if (matchLine != null) {
				pmsiPipedWriter.writeLineElement(matchLine);
				changeState(EnumSignal.SIGNAL_RSF_END_LINES);
			} else
				throw new PmsiIOException("Lecteur RSF : Ligne non reconnue");
			break;
		case WAIT_ENDLINE:
			// On vérifie qu'il ne reste rien
			if (getLineSize() != 0)
				throw new PmsiIOException("trop de caractères dans la ligne");
			changeState(EnumSignal.SIGNAL_ENDLINE);
			readNewLine();
			break;
		case STATE_EMPTY_FILE:
			throw new PmsiIOException("Lecteur RSF : Fichier vide");
		default:
			throw new RuntimeException("Cas non prévu par la machine à états");
		}
	}

	@Override
	public void endOfFile() throws PmsiIOException {
		changeState(EnumSignal.SIGNAL_EOF);
	}

	@Override
	public void finish() throws Exception {
		pmsiPipedWriter.writeEndDocument();
	}
	
	@Override
	public void close() throws PmsiPipedIOException {
		pmsiPipedWriter.close();
	}
}
