package aider.org.pmsi.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import aider.org.machinestate.MachineStateException;
import aider.org.pmsi.exceptions.PmsiParserException;
import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009m;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009Header;
import aider.org.pmsi.writer.PmsiWriter;


/**
 * Définition de la lecture d'un RSF version 2009
 * @author delabre
 *
 */
public class PmsiRSF2009Parser extends PmsiParser<PmsiRSF2009Parser.EnumState, PmsiRSF2009Parser.EnumSignal> {

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
	 * Nom identifiant cette classe de PmsiParser
	 */
	private static final String name = "RSF2009";
	
	/**
	 * Objet de transfert de données
	 */
	private PmsiWriter writer = null;
	
	/**
	 * Permet de se souvenir quelle ligne a été insérée en dernier
	 */
	private Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	/**
	 * Constructeur
	 * @param reader
	 * @throws PmsiWriterException 
	 */
	public PmsiRSF2009Parser(Reader reader, PmsiWriter pmsiPipedWriter) throws PmsiWriterException {
		super(reader, EnumState.STATE_READY, EnumState.STATE_FINISHED, EnumSignal.SIGNAL_EOF);
	
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
		this.writer = pmsiPipedWriter;
	}
	
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la machine à états
	 * @throws PmsiWriterException 
	 * @throws IOException 
	 * @throws PmsiFileNotReadable 
	 */
	public void process() throws MachineStateException {
		PmsiLineType matchLine = null;

		try {
			switch(getState()) {
			case STATE_READY:
				// Le parseur est en attente de la prochaine ligne
				writer.writeStartDocument(name, new String[0], new String[0], getLineNumber());
				changeState(EnumSignal.SIGNAL_START);
				readNewLine();
				break;
			case WAIT_RSF_HEADER:
				// Attente de la ligne de header
				matchLine = parseLine();
				if (matchLine != null) {
					lastLine.add(matchLine);
					writer.writeLineElement(matchLine, getLineNumber());
					changeState(EnumSignal.SIGNAL_RSF_END_HEADER);
				} else {
					throw new PmsiParserException("Entête du fichier non trouvée");
				}
				break;
			case WAIT_RSF_LINES:
				// Attente d'une ligne A, B, C, H ou M
				matchLine = parseLine();
				if (matchLine != null) {
					if (matchLine instanceof PmsiRsf2009a) {
						// Si on a une ligne A, il faut fermer les lignes précédentes jusqu'au header
						while (!(lastLine.lastElement() instanceof PmsiRsf2009Header)) {
							lastLine.pop();
							writer.writeEndElement(getLineNumber());
						}
					} else {
						// Si on a une ligne autre que A, il faut fermer les lignes précédentes jusqu'à une ligne A
						while (!(lastLine.lastElement() instanceof PmsiRsf2009a)) {
							lastLine.pop();
							writer.writeEndElement(getLineNumber());
						}
					}
					lastLine.add(matchLine);
					writer.writeLineElement(matchLine, getLineNumber());
					changeState(EnumSignal.SIGNAL_RSF_END_LINES);
				} else {
					throw new PmsiParserException("Ligne non reconnue");
	
				}
				break;
			case WAIT_ENDLINE:
				// Attente de la fin de la ligne
				if (getLineSize() != 0)
					throw new PmsiParserException("trop de caractères dans la ligne");
				changeState(EnumSignal.SIGNAL_ENDLINE);
				readNewLine();
				break;
			case STATE_EMPTY_FILE:
				throw new PmsiParserException("Fichier vide");
			default:
				throw new PmsiParserException("Cas non prévu par la machine à états");
			}
		} catch (PmsiWriterException e) {
			throw new MachineStateException(e);
		} catch (PmsiParserException e) {
			throw new MachineStateException(e);
		}
	}

	@Override
	public void finish() throws MachineStateException {
		try {
			// Fermeture de tous les éléments ouverts :
			while (!lastLine.isEmpty()) {
				lastLine.pop();
				writer.writeEndElement(getLineNumber());
			}
			// Fermeture du document
			writer.writeEndDocument(getLineNumber());
		} catch (Exception e) {
			throw new MachineStateException(e);
		}
	}
	
	@Override
	public void close() throws MachineStateException {
	}
}
