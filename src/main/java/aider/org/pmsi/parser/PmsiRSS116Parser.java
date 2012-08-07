package aider.org.pmsi.parser;

import java.io.IOException;
import java.io.Reader;

import aider.org.machinestate.MachineStateException;
import aider.org.pmsi.exceptions.PmsiReaderException;
import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRss116Header;
import aider.org.pmsi.parser.linestypes.PmsiRss116Acte;
import aider.org.pmsi.parser.linestypes.PmsiRss116Da;
import aider.org.pmsi.parser.linestypes.PmsiRss116Dad;
import aider.org.pmsi.parser.linestypes.PmsiRss116Main;
import aider.org.pmsi.writer.PmsiWriter;



/**
 * Classe étendant la classe abstraite de PmsiParser et permettant de lire un
 * fichier RSS (version 116). 
 * @author delabre
 *
 */
public class PmsiRSS116Parser extends aider.org.pmsi.parser.PmsiParser<PmsiRSS116Parser.EnumState, PmsiRSS116Parser.EnumSignal> {

	/**
	 * Liste des états de la machine à états
	 * @author delabre
	 *
	 */
	public enum EnumState {
		STATE_READY, STATE_FINISHED,
		WAIT_RSS_HEADER, WAIT_RSS_HEADER_ENDLINE,
		WAIT_RSS_MAIN, WAIT_RSS_DA, WAIT_RSS_DAD,
		WAIT_RSS_ACTE, WAIT_RSS_ENDLINE, STATE_EMPTY_FILE}
	
	/**
	 * Liste des signaux
	 * @author delabre
	 *
	 */
	public enum EnumSignal {
		SIGNAL_START, // STATE_READY -> WAIT_RSS_HEADER
		SIGNAL_RSS_END_HEADER, // WAIT_RSS_HEADER -> WAIT_RSS_HEADER_ENDLINE
		SIGNAL_RSS_HEADER_NEWLINE, // WAIT_RSS_HEADER_ENDLINE -> WAIT_RSS_MAIN
		SIGNAL_RSS_END_MAIN, // WAIT_RSS_MAIN -> WAIT_RSS_DA
		SIGNAL_RSS_END_DA, // WAIT_RSS_DA -> WAIT_RSS_DAD
		SIGNAL_RSS_END_DAD, // WAIT_RSS_DAD -> WAIT_RSS_ACTE
		SIGNAL_RSS_END_ACTE, // WAIT_RSS_ACTE -> WAIT_RSS_FIN_LIGNE
		SIGNAL_RSS_NEWLINE, // WAIT_RSS_FIN_LIGNE -> WAIT_RSS_MAIN
		SIGNAL_EOF
	}
	
	/**
	 * Nombre de diagnostics associés restant à lire
	 */
	private int nbDaRestants;
	
	/**
	 * Nombre de diagnostics associés documentaires à lire
	 */
	private int nbDaDRestants;
	
	/**
	 * Nombre d'actes restant à lire
	 */
	private int nbZARestants;
	
	/**
	 * Objet de transfert de données
	 */
	private PmsiWriter writer = null;

	/**
	 * Nom identifiant la classe
	 */
	private static final String name = "RSS116"; 
	
	/**
	 * Constructeur d'un lecteur de fichier rss
	 * @param reader
	 * @throws PmsiWriterException 
	 */
	public PmsiRSS116Parser(Reader reader, PmsiWriter writer) throws PmsiWriterException {
		super(reader, EnumState.STATE_READY, EnumState.STATE_FINISHED, EnumSignal.SIGNAL_EOF);
	
		// Indication des différents types de ligne que l'on peut rencontrer
		addLineType(EnumState.WAIT_RSS_HEADER, new PmsiRss116Header());
		addLineType(EnumState.WAIT_RSS_MAIN, new PmsiRss116Main());
		addLineType(EnumState.WAIT_RSS_DA, new PmsiRss116Da());
		addLineType(EnumState.WAIT_RSS_DAD, new PmsiRss116Dad());
		addLineType(EnumState.WAIT_RSS_ACTE, new PmsiRss116Acte());
		
		// Définition des états et des signaux de la machine à états
		addTransition(EnumSignal.SIGNAL_START, EnumState.STATE_READY, EnumState.WAIT_RSS_HEADER);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSS_HEADER, EnumState.STATE_EMPTY_FILE);
		addTransition(EnumSignal.SIGNAL_RSS_END_HEADER, EnumState.WAIT_RSS_HEADER, EnumState.WAIT_RSS_HEADER_ENDLINE);
		addTransition(EnumSignal.SIGNAL_RSS_HEADER_NEWLINE, EnumState.WAIT_RSS_HEADER_ENDLINE, EnumState.WAIT_RSS_MAIN);
		addTransition(EnumSignal.SIGNAL_RSS_END_MAIN, EnumState.WAIT_RSS_MAIN, EnumState.WAIT_RSS_DA);
		addTransition(EnumSignal.SIGNAL_RSS_END_DA, EnumState.WAIT_RSS_DA, EnumState.WAIT_RSS_DAD);
		addTransition(EnumSignal.SIGNAL_RSS_END_DAD, EnumState.WAIT_RSS_DAD, EnumState.WAIT_RSS_ACTE);
		addTransition(EnumSignal.SIGNAL_RSS_END_ACTE, EnumState.WAIT_RSS_ACTE, EnumState.WAIT_RSS_ENDLINE);
		addTransition(EnumSignal.SIGNAL_RSS_NEWLINE, EnumState.WAIT_RSS_ENDLINE, EnumState.WAIT_RSS_MAIN);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSS_MAIN, EnumState.STATE_FINISHED);

		// Récupération de la classe de transfert en base de données
		this.writer = writer;
	}
			
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la machine à états
	 * @throws PmsiWriterException
	 * @throws IOException
	 * @throws PmsiReaderException
	 * @throws MachineStateException 
	 */
	public void process() throws PmsiWriterException, PmsiReaderException, MachineStateException {
		PmsiLineType matchLine = null;
		
		switch(getState()) {
		case STATE_READY:
			// L'état initial nous demande de lire un nouvelle ligne
			writer.writeStartDocument(name, new String[0], new String[0]);
			changeState(EnumSignal.SIGNAL_START);
			readNewLine();
			break;
		case WAIT_RSS_HEADER:
			matchLine = parseLine();
			if (matchLine != null) {
				writer.writeLineElement(matchLine);
				changeState(EnumSignal.SIGNAL_RSS_END_HEADER);
			} else {
				throw (PmsiReaderException) new PmsiReaderException("Entête du fichier non trouvée").
					setXmlMessage("<rss v=\"116\"><parsingerror>Entête du fichier non trouvée</parsingerror></rss>");
			}
			break;
		case WAIT_RSS_MAIN:
			matchLine = parseLine();
			if (matchLine != null) {
				writer.writeLineElement(matchLine);
				nbDaRestants = Integer.parseInt(matchLine.getContent()[26]);
				nbDaDRestants = Integer.parseInt(matchLine.getContent()[27]);
				nbZARestants = Integer.parseInt(matchLine.getContent()[28]);
				changeState(EnumSignal.SIGNAL_RSS_END_MAIN);
			} else {
				throw (PmsiReaderException) new PmsiReaderException("Première partie de ligne RSS non trouvée").
					setXmlMessage("<rss v=\"116\"><parsingerror>Première partie de ligne RSS non trouvée</parsingerror></rss>");
			}
			break;
		case WAIT_RSS_DA:
			if (nbDaRestants == 0)
				changeState(EnumSignal.SIGNAL_RSS_END_DA);
			else {
				matchLine = parseLine();
				if (matchLine != null) {
					writer.writeLineElement(matchLine);
					nbDaRestants -= 1;
				} else {
					throw (PmsiReaderException) new PmsiReaderException("DA non trouvés dans ligne RSS").
						setXmlMessage("<rss v=\"116\"><parsingerror>DA non trouvés dans ligne RSS</parsingerror></rss>");
				}
			}
			break;
		case WAIT_RSS_DAD:
			if (nbDaDRestants == 0)
				changeState(EnumSignal.SIGNAL_RSS_END_DAD);
			else {
				matchLine = parseLine();
				if (matchLine != null) {
					writer.writeLineElement(matchLine);
					nbDaDRestants -= 1;
				} else {
					throw (PmsiReaderException) new PmsiReaderException("DAD non trouvés dans ligne RSS").
						setXmlMessage("<rss v=\"116\"><parsingerror>DAD non trouvés dans ligne RSS</parsingerror></rss>");
				}
			}
			break;
		case WAIT_RSS_ACTE:
			if (nbZARestants == 0)
				changeState(EnumSignal.SIGNAL_RSS_END_ACTE);
			else {
				matchLine = parseLine();
				if (matchLine != null) {
					writer.writeLineElement(matchLine);
					nbZARestants -= 1;
				} else {
					throw (PmsiReaderException) new PmsiReaderException("Actes non trouvés dans ligne RSS").
						setXmlMessage("<rss v=\"116\"><parsingerror>Actes non trouvés dans ligne RSS</parsingerror></rss>");
				}
			}
			break;
		case WAIT_RSS_ENDLINE:
			// On vérifie qu'il ne reste rien
			if (getLineSize() != 0)
				throw (PmsiReaderException) new PmsiReaderException("trop de caractères dans la ligne").
				setXmlMessage("<rss v=\"116\"><parsingerror>trop de caractères dans la ligne</parsingerror></rss>");
			changeState(EnumSignal.SIGNAL_RSS_NEWLINE);
			readNewLine();
			break;
		case WAIT_RSS_HEADER_ENDLINE:
			// On vérifie qu'il ne reste rien
			if (getLineSize() != 0) {
				throw (PmsiReaderException) new PmsiReaderException("trop de caractères dans la ligne").
					setXmlMessage("<rss v=\"116\"><parsingerror>trop de caractères dans la ligne</parsingerror></rss>");
			}
			changeState(EnumSignal.SIGNAL_RSS_HEADER_NEWLINE);
			readNewLine();
			break;			
		case STATE_EMPTY_FILE:
			throw (PmsiReaderException) new PmsiReaderException("Fichier vide").
				setXmlMessage("<rss v=\"116\"><parsingerror>Fichier vide</parsingerror></rss>");
		default:
			throw new RuntimeException("Cas non prévu par la machine à états");
		}
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
