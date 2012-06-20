package org.aider.pmsi2sql;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.aider.pmsi2sql.linetypes.PmsiRssActe;
import org.aider.pmsi2sql.linetypes.PmsiRssDa;
import org.aider.pmsi2sql.linetypes.PmsiRssDad;
import org.aider.pmsi2sql.linetypes.PmsiRssMain;

import aider.org.pmsi.parser.PmsiLineType;
import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.PmsiRssHeader;

/**
 * Classe étendant la classe abstraite de PmsiReader et permettant de lire un
 * fichier RSS (version 116). 
 * @author delabre
 *
 */
public class PmsiRSS116Reader extends aider.org.pmsi.parser.PmsiReader<PmsiRSS116Reader.EnumState, PmsiRSS116Reader.EnumSignal> {

	public enum EnumState {
		STATE_READY, STATE_EOF, STATE_FINISHED,
		WAIT_RSS_HEADER, WAIT_RSS_MAIN, WAIT_RSS_DA, WAIT_RSS_DAD,
		WAIT_RSS_ACTE, WAIT_RSS_FIN_LIGNE, STATE_EMPTY_FILE}
	
	public enum EnumSignal {
		SIGNAL_START, // STATE_READY -> STATE_RSS_HEADER
		SIGNAL_RSS_END_HEADER, // STATE_RSS_HEADER -> STATE_RSS_MAIN
		SIGNAL_RSS_END_MAIN, // STATE_RSS_MAIN -> STATE_RSS_DA
		SIGNAL_RSS_END_DA, // STATE_RSS_DA -> STATE_RSS_DAD
		SIGNAL_RSS_END_DAD, // STATE_RSS_DAD -> STATE_RSS_ACTE
		SIGNAL_RSS_END_ACTE, // STATE_RSS_ACTE -> STATE_RSS_FIN_LIGNE
		SIGNAL_RSS_NEWLINE, //TATE_RSS_FIN_LIGNE -> STATE_RSS_MAIN
		SIGNAL_EOF
	}
	
	/**
	 * Nombre de diagnostics associés restant à lire
	 */
	int nbDaRestants;
	
	/**
	 * Nombre de diagnostics associés documentaires à lire
	 */
	int nbDaDRestants;
	/**
	 * Nombre d'actes restant à lire
	 */
	int nbZARestants;
	
	/**
	 * Constructeur d'un lecteur de fichier rss
	 */
	public PmsiRSS116Reader(Reader reader) {
		super(reader, EnumState.STATE_READY, EnumState.STATE_FINISHED, EnumSignal.SIGNAL_EOF);
	
		// Indication des différents types de ligne que l'on peut rencontrer
		addLineType(EnumState.WAIT_RSS_HEADER, new PmsiRssHeader());
		addLineType(EnumState.WAIT_RSS_MAIN, new PmsiRssMain());
		addLineType(EnumState.WAIT_RSS_DA, new PmsiRssDa());
		addLineType(EnumState.WAIT_RSS_DAD, new PmsiRssDad());
		addLineType(EnumState.WAIT_RSS_ACTE, new PmsiRssActe());
		
		// Définition des états et des signaux de la machine à états
		addTransition(EnumSignal.SIGNAL_START, EnumState.STATE_READY, EnumState.WAIT_RSS_HEADER);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSS_HEADER, EnumState.STATE_EMPTY_FILE);
		addTransition(EnumSignal.SIGNAL_RSS_END_HEADER, EnumState.WAIT_RSS_HEADER, EnumState.WAIT_RSS_MAIN);
		addTransition(EnumSignal.SIGNAL_RSS_END_MAIN, EnumState.WAIT_RSS_MAIN, EnumState.WAIT_RSS_DA);
		addTransition(EnumSignal.SIGNAL_RSS_END_DA, EnumState.WAIT_RSS_DA, EnumState.WAIT_RSS_DAD);
		addTransition(EnumSignal.SIGNAL_RSS_END_DAD, EnumState.WAIT_RSS_DAD, EnumState.WAIT_RSS_ACTE);
		addTransition(EnumSignal.SIGNAL_RSS_END_ACTE, EnumState.WAIT_RSS_ACTE, EnumState.WAIT_RSS_FIN_LIGNE);
		addTransition(EnumSignal.SIGNAL_RSS_NEWLINE, EnumState.WAIT_RSS_FIN_LIGNE, EnumState.WAIT_RSS_MAIN);
		addTransition(EnumSignal.SIGNAL_EOF, EnumState.WAIT_RSS_MAIN, EnumState.STATE_FINISHED);
	}
			
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la machine à états
	 * @throws PmsiFileNotInserable si une erreur empêchant l'insertion de de fichier comme un fichier
	 *   de type RSS est survenue 
	 */
	public void process() throws PmsiFileNotReadable, PmsiFileNotInserable {
		PmsiLineType matchLine = null;
		
		switch(getState()) {
		case STATE_READY:
			// L'état initial nous demande de lire un nouvelle ligne
			changeState(EnumSignal.SIGNAL_START);
			readNewLine();
			break;
		case WAIT_RSS_HEADER:
			matchLine = parseLine();
			if (matchLine != null) {
				System.out.println(matchLine.getContent());
				changeState(EnumSignal.SIGNAL_RSS_END_HEADER);
				readNewLine();
			} else
				throw new PmsiFileNotReadable("Lecteur RSS 116 : Entête du fichier non trouvée");
			break;
		case WAIT_RSS_MAIN:
			matchLine = parseLine();
			if (matchLine != null) {
				System.out.println(matchLine.getContent());
				nbDaRestants = Integer.parseInt(matchLine.getContent()[2]);
				nbDaDRestants = Integer.parseInt(matchLine.getContent()[2]);
				nbZARestants = Integer.parseInt(matchLine.getContent()[2]);
				changeState(EnumSignal.SIGNAL_RSS_END_MAIN);
			} else
				throw new PmsiFileNotReadable("Lecteur RSS 116 : Première partie de ligne RSS non trouv�e");
			break;
		case WAIT_RSS_DA:
			if (nbDaRestants == 0)
				changeState(EnumSignal.SIGNAL_RSS_END_DA);
			else {
				matchLine = parseLine();
				if (matchLine != null) {
					nbDaRestants -= 1;
				} else
					throw new PmsiFileNotInserable("Lecteur RSS 116 : DA non trouvés dans ligne RSS");
			}
			break;
		case WAIT_RSS_DAD:
			if (nbDaDRestants == 0)
				changeState(EnumSignal.SIGNAL_RSS_END_DAD);
			else {
				matchLine = parseLine();
				if (matchLine != null) {
					nbDaDRestants -= 1;
				} else
					throw new PmsiFileNotInserable("Lecteur RSS : DAD non trouvés dans ligne RSS");
			}
			break;
		case WAIT_RSS_ACTE:
			if (nbZARestants == 0)
				changeState(EnumSignal.SIGNAL_RSS_END_ACTE);
			else {
				matchLine = parseLine();
				if (matchLine != null) {
					nbZARestants -= 1;
				} else
					throw new PmsiFileNotInserable("Lecteur RSS : Actes non trouvés dans ligne RSS");
			}
			break;
		case WAIT_RSS_FIN_LIGNE:
			changeState(EnumSignal.SIGNAL_RSS_NEWLINE);
			readNewLine();
			break;
		case STATE_EMPTY_FILE:
			throw new PmsiFileNotReadable("Lecteur RSS : ", new IOException("Fichier vide"));
		default:
			throw new RuntimeException("Cas non prévu par la machine à états");
		}
	}

	/**
	 * Fonction exécutée lorsque la fin du flux est rencontrée
	 */
	public void EndOfFile() throws Exception {
		changeState(EnumSignal.SIGNAL_EOF);		
	}

	
}
