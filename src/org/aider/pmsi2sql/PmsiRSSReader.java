package org.aider.pmsi2sql;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.aider.pmsi2sql.linetypes.PmsiLineType;
import org.aider.pmsi2sql.linetypes.PmsiRssActe;
import org.aider.pmsi2sql.linetypes.PmsiRssDa;
import org.aider.pmsi2sql.linetypes.PmsiRssDad;
import org.aider.pmsi2sql.linetypes.PmsiRssHeader;
import org.aider.pmsi2sql.linetypes.PmsiRssMain;

/**
 * Classe étendant la classe abstraite de PmsiReader et permettant de lire un
 * fichier RSS (version 116). 
 * @author delabre
 *
 */
public class PmsiRSSReader extends PmsiReader {

	/**
	 * Etat d'attente de lecture de l'entête du fichier RSS
	 */
	public static final int STATE_RSS_HEADER = 1;
	/**
	 * Etat d'attente de lecture de la partie principale et invariante d'une ligne du RSS (diagnostic principal)
	 */
	public static final int STATE_RSS_MAIN = 2;
	/**
	 * Etat d'attente de lecture des diagnostics associés de la ligne RSS
	 */
	public static final int STATE_RSS_DA = 3;
	/**
	 * Etat d'attente de lecture des diagnostics documentaires de la ligne RSS
	 */
	public static final int STATE_RSS_DAD = 4;
	/**
	 * Etat d'attente de lecture des actes de la ligne RSS
	 */
	public static final int STATE_RSS_ACTE = 5;
	/**
	 * Etat d'attente de lecture de la fin de la ligne RSS
	 */
	public static final int STATE_RSS_FIN_LIGNE = 6;
	/**
	 * Etat permettant d'indiquer que le fichier est vide
	 */
	public static final int STATE_EMPTY_FILE = 7;
	
	/**
	 * Signal permettant une modification d'état;
	 * SIGNAL_START : STATE_READY -> STATE_RSS_HEADER
	 */
	public static final int SIGNAL_START = 1;
	/**
	 * Signal permettant une modification d'état;
	 * SIGNAL_RSS_END_HEADER : STATE_RSS_HEADER -> STATE_RSS_MAIN
	 */
	public static final int SIGNAL_RSS_END_HEADER = 6;
	/**
	 * Signal permettant une modification d'état;
	 * SIGNAL_RSS_END_MAIN : STATE_RSS_MAIN -> STATE_RSS_DA
	 */
	public static final int SIGNAL_RSS_END_MAIN = 2;
	/**
	 * Signal permettant une modification d'état;
	 * SIGNAL_RSS_END_DA : STATE_RSS_DA -> STATE_RSS_DAD
	 */
	public static final int SIGNAL_RSS_END_DA = 3;
	/**
	 * Signal permettant une modification d'état;
	 * SIGNAL_RSS_END_DAD : STATE_RSS_DAD -> STATE_RSS_ACTE
	 */
	public static final int SIGNAL_RSS_END_DAD = 4;
	/**
	 * Signal permettant une modification d'état;
	 * SIGNAL_RSS_END_ACTE : STATE_RSS_ACTE -> STATE_RSS_FIN_LIGNE
	 */
	public static final int SIGNAL_RSS_END_ACTE = 5;
	/**
	 * Signal permettant une modification d'état;
	 * SIGNAL_RSS_NEWLINE : STATE_RSS_FIN_LIGNE -> STATE_RSS_MAIN
	 */
	public static final int SIGNAL_RSS_NEWLINE = 6;
	
	/**
	 * Nombre de diagnostics associés lus
	 */
	int NbDAFaits;
	/**
	 * Nombre de diagnostics associés restant à lire
	 */
	int NbDaRestants;
	/**
	 * Nombre de diagnostics associés documentaires lus
	 */
	int NbDADFaits;
	/**
	 * Nombre de diagnostics associés documentaires restant à lire
	 */
	int NbDaDRestants;
	/**
	 * Nombre d'actes lus
	 */
	int NbZAFaits;
	/**
	 * Nombre d'actes restant à lire
	 */
	int NbZARestants;
	
	/**
	 * Constructeur d'un lecteur de fichier rss
	 * @param MyReader Flux à lire (habituellement un fichier)
	 * @param myConn Connexion à la base de données à utiliser
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public PmsiRSSReader(Reader MyReader, Connection myConn) throws IOException, ClassNotFoundException, SQLException {
		super(MyReader, myConn);

		// Indication des différents types de ligne que l'on peut rencontrer
		addLineType(STATE_RSS_HEADER, new PmsiRssHeader());
		addLineType(STATE_RSS_MAIN, new PmsiRssMain());
		addLineType(STATE_RSS_DA, new PmsiRssDa());
		addLineType(STATE_RSS_DAD, new PmsiRssDad());
		addLineType(STATE_RSS_ACTE, new PmsiRssActe());
		
		// Définition des états et des signaux de la machine à états
		addTransition(SIGNAL_START, STATE_READY, STATE_RSS_HEADER);
		addTransition(SIGNAL_EOF, STATE_RSS_HEADER, STATE_EMPTY_FILE);
		addTransition(SIGNAL_RSS_END_HEADER, STATE_RSS_HEADER, STATE_RSS_MAIN);
		addTransition(SIGNAL_RSS_END_MAIN, STATE_RSS_MAIN, STATE_RSS_DA);
		addTransition(SIGNAL_RSS_END_DA, STATE_RSS_DA, STATE_RSS_DAD);
		addTransition(SIGNAL_RSS_END_DAD, STATE_RSS_DAD, STATE_RSS_ACTE);
		addTransition(SIGNAL_RSS_END_ACTE, STATE_RSS_ACTE, STATE_RSS_FIN_LIGNE);
		addTransition(SIGNAL_RSS_NEWLINE, STATE_RSS_FIN_LIGNE, STATE_RSS_MAIN);
		addTransition(SIGNAL_EOF, STATE_RSS_MAIN, STATE_FINISHED);
	}
		
	/**
	 * Fonction appelée par {@link #run()} pour réaliser chaque étape de la macine à états
	 * @throws PmsiFileNotInserable si une erreur empêchant l'insertion de de fichier comme un fichier
	 *   de type RSS est survenue 
	 */
	public void process() throws PmsiFileNotReadable, PmsiFileNotInserable {
		// Liste permettant d'informer la fonction parseLine des différentes lignes que l'on peut rencontrer
		Vector<Integer> MyV = new Vector<Integer>();
		// Objet récupérant le type de ligne est les informations contenues dans un ligne lue
		PmsiLineType MyMatch;
		switch(getState()) {
		case STATE_READY:
			changeState(SIGNAL_START);
			readNewLine();
			break;
		case STATE_RSS_HEADER:
			MyV.add(STATE_RSS_HEADER);
			MyMatch = parseLine(MyV);
			if (MyMatch != null) {
				try {
					MyMatch.insertSQLLine(getSqlConnection());
				} catch (SQLException e) {
					throw new PmsiFileNotInserable("Lecteur entête RSS : ", e);
				}
				changeState(SIGNAL_RSS_END_HEADER);
				readNewLine();
			} else
				throw new PmsiFileNotReadable("Lecteur RSS : Entête du fichier non trouvé");
			break;
		case STATE_RSS_MAIN:
			MyV.add(STATE_RSS_MAIN);
			MyMatch = parseLine(MyV); 
			if (MyMatch != null) {
				try {
					MyMatch.insertSQLLine(getSqlConnection());
				} catch (SQLException e) {
					throw new PmsiFileNotInserable("Lecteur Main RSS : ", e);
				}
				NbDAFaits = 0;
				NbDaRestants = Integer.parseInt(MyMatch.getValue("NbDA"));
				NbDADFaits = 0;
				NbDaDRestants = Integer.parseInt(MyMatch.getValue("NbDAD"));
				NbZAFaits = 0;
				NbZARestants = Integer.parseInt(MyMatch.getValue("NbZA"));
				changeState(SIGNAL_RSS_END_MAIN);
			} else
				throw new PmsiFileNotReadable("Lecteur RSS : Première partie de ligne RSS non trouvée");
			break;
		case STATE_RSS_DA:
			if (NbDaRestants == 0)
				changeState(SIGNAL_RSS_END_DA);
			else {
				MyV.add(STATE_RSS_DA);
				MyMatch = parseLine(MyV);
				if (MyMatch != null) {
					try {
						MyMatch.insertSQLLine(getSqlConnection());
					} catch (SQLException e) {
						throw new PmsiFileNotInserable("Lecteur DA RSS : ", e);
					}
					NbDAFaits += 1;
					NbDaRestants -= 1;
				} else
					throw new PmsiFileNotInserable("Lecteur RSS : DA non trouvés dans ligne RSS");
			}
			break;
		case STATE_RSS_DAD:
			if (NbDaDRestants == 0)
				changeState(SIGNAL_RSS_END_DAD);
			else {
				MyV.add(STATE_RSS_DAD);
				MyMatch = parseLine(MyV);
				if (MyMatch != null) {
					try {
						MyMatch.insertSQLLine(getSqlConnection());
					} catch (SQLException e) {
						throw new PmsiFileNotInserable("Lecteur DAD RSS : ", e);
					}
					NbDADFaits += 1;
					NbDaDRestants -= 1;
				} else
					throw new PmsiFileNotInserable("Lecteur RSS : DAD non trouvés dans ligne RSS");
			}
			break;
		case STATE_RSS_ACTE:
			if (NbZARestants == 0)
				changeState(SIGNAL_RSS_END_ACTE);
			else {
				MyV.add(STATE_RSS_ACTE);
				MyMatch = parseLine(MyV);
				if (MyMatch != null) {
					try {
						MyMatch.insertSQLLine(getSqlConnection());
					} catch (SQLException e) {
						throw new PmsiFileNotInserable("Lecteur Actes RSS : ", e);
					}
					NbZAFaits += 1;
					NbZARestants -= 1;
				} else
					throw new PmsiFileNotInserable("Lecteur RSS : Actes non trouvés dans ligne RSS");
			}
			break;
		case STATE_RSS_FIN_LIGNE:
			changeState(SIGNAL_RSS_NEWLINE);
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
		changeState(SIGNAL_EOF);		
	}

	
}
