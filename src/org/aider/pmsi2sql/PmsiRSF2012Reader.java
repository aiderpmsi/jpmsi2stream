package org.aider.pmsi2sql;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.aider.pmsi2sql.linetypes.PmsiRsfa2012;
import org.aider.pmsi2sql.linetypes.PmsiRsfb;
import org.aider.pmsi2sql.linetypes.PmsiRsfc;
import org.aider.pmsi2sql.linetypes.PmsiRsfh;
import org.aider.pmsi2sql.linetypes.PmsiRsfHeader;
import org.aider.pmsi2sql.linetypes.PmsiRsfl2012;
import org.aider.pmsi2sql.linetypes.PmsiRsfm;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Classe �tendant la classe abstraite de PmsiReader et permettant de lire un
 * fichier RSF (version mars 2009). Une nouvelle version en mars 2012 devrait
 * �tre impossible � lire avec cette classe et une nouvelle classe RSF
 * devrait �tre �crite (par ex PmsiRSF2012Reader) 
 * @author delabre
 *
 */
public class PmsiRSF2012Reader extends PmsiReader {

	/**
	 * Etat d'attente de lecture de l'ent�te du fichier RSF
	 */
	public static final int STATE_RSF_HEADER = 1;
	/**
	 * Etat d'attente de lecture d'une ligne apr�s l'ent�te du fichier RSF
	 */
	public static final int STATE_RSF_LINES = 2;
	/**
	 * Etat permettant d'indiquer que le fichier est vide
	 */
	public static final int STATE_EMPTY_FILE = 4;
	
	/**
	 * Signal permettant une modification d'�tat :
	 * SIGNAL_START : STATE_READY -> STATE_RSS_HEADER
	 */
	public static final int SIGNAL_START = 1;
	/**
	 * Signal permettant une modification d'�tat :
	 * SIGNAL_RSF_END_HEADER : STATE_RSF_HEADER -> STATE_RSF_LINES
	 */
	public static final int SIGNAL_RSF_END_HEADER = 2;
	/**
	 * Signal permettant une modification d'�tat :
	 * SIGNAL_RSF_END_LINES : STATE_RSF_LINES -> STATE_RSF_LINES
	 */
	public static final int SIGNAL_RSF_END_LINES = 3;

	/**
	 * Identifiant de l'ent�te d'un fichier RSF dans la liste des lignes � lire
	 * par {@link #parseLine(Vector)}
	 */
	public static final int RSFHEADER = 1;
	/**
	 * Identifiant d'une ligne A de RSF dans la liste des lignes � lire
	 * par {@link #parseLine(Vector)}
	 */
	public static final int RSFA2012 = 2;
	/**
	 * Identifiant d'une ligne B de RSF dans la liste des lignes � lire
	 * par {@link #parseLine(Vector)}
	 */
	public static final int RSFB = 3;
	/**
	 * Identifiant d'une ligne C de RSF dans la liste des lignes � lire
	 * par {@link #parseLine(Vector)}
	 */
	public static final int RSFC = 4;
	/**
	 * Identifiant d'une ligne H de RSF dans la liste des lignes � lire
	 * par {@link #parseLine(Vector)}
	 */
	public static final int RSFH = 5;
	/**
	 * Identifiant d'une ligne M de RSF dans la liste des lignes � lire
	 * par {@link #parseLine(Vector)}
	 */
	public static final int RSFM = 6;
		
	/**
	 * Identifiant d'une ligne L de RSF dans la liste des lignes � lire
	 * par {@link #parseLine(Vector)}
	 */
	public static final int RSFL2012 = 7;

	/**
	 * Constructeur d'un lecteur de fichier rsf
	 * @param MyReader Flux � lire (habituellement un fichier)
	 * @param myConn Connexion � la base de donn�es � utiliser
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public PmsiRSF2012Reader(Reader MyReader, Connection myConn) throws IOException, ClassNotFoundException, SQLException {
		super(MyReader, myConn);

		// Indication des diff�rents types de ligne que l'on peut rencontrer
		addLineType(RSFHEADER, new PmsiRsfHeader());
		addLineType(RSFA2012, new PmsiRsfa2012());
		addLineType(RSFB, new PmsiRsfb());
		addLineType(RSFC, new PmsiRsfc());
		addLineType(RSFH, new PmsiRsfh());
		addLineType(RSFM, new PmsiRsfm());
		addLineType(RSFL2012, new PmsiRsfl2012());
		
		// D�finition des �tats et des signaux de la machine � �tats
		addTransition(SIGNAL_START, STATE_READY, STATE_RSF_HEADER);
		addTransition(SIGNAL_EOF, STATE_RSF_HEADER, STATE_EMPTY_FILE);
		addTransition(SIGNAL_RSF_END_HEADER, STATE_RSF_HEADER, STATE_RSF_LINES);
		addTransition(SIGNAL_RSF_END_LINES, STATE_RSF_LINES, STATE_RSF_LINES);
		addTransition(SIGNAL_EOF, STATE_RSF_LINES, STATE_FINISHED);
	}
	
	/**
	 * Fonction appel�e par {@link #run()} pour r�aliser chaque �tape de la macine � �tats
	 * @throws PmsiFileNotInserable si une erreur emp�chant l'insertion de de fichier comme un fichier
	 *   de type RSF est survenue 
	 */
	public void process() throws PmsiFileNotReadable, PmsiFileNotInserable {
		// Liste permettant d'informer la fonction parseLine des diff�rentes lignes que l'on peut rencontrer
		Vector<Integer> MyV = new Vector<Integer>();
		// Objet r�cup�rant le type de ligne est les informations contenues dans un ligne lue
		PmsiLineType MyMatch;
		switch(getState()) {
		case STATE_READY:
			changeState(SIGNAL_START);
			readNewLine();
			break;
		case STATE_RSF_HEADER:
			MyV.add(RSFHEADER);
			MyMatch = parseLine(MyV);
			if (MyMatch != null) {
				try {
					MyMatch.insertSQLLine(getSqlConnection());
				} catch (SQLException e) {
					throw new PmsiFileNotInserable("Lecteur ent�te RSF : ", e);
				}
				changeState(SIGNAL_RSF_END_HEADER);
				readNewLine();
			} else
				throw new PmsiFileNotReadable("Lecteur RSF : Ent�te du fichier non trouv�");
			break;
		case STATE_RSF_LINES:
			MyV.add(RSFA2012);
			MyV.add(RSFB);
			MyV.add(RSFC);
			MyV.add(RSFH);
			MyV.add(RSFM);
			MyV.add(RSFL2012);
			MyMatch = parseLine(MyV);
			if (MyMatch != null) {
				try {
					MyMatch.insertSQLLine(getSqlConnection());
				} catch (SQLException e) {
					throw new PmsiFileNotInserable("Lecteur ligne RSF : ", e);
				}
				changeState(SIGNAL_RSF_END_LINES);
				readNewLine();
			} else {
				System.out.println(getLine() + "\n");
				// Une ligne inconnue peut �tre li� � un nouveau format, c'est donc pas
				// une erreur qui emp�che d'ins�rer le fichier
				throw new PmsiFileNotReadable("Lecteur RSF : Ligne inconnue");
			}
			break;
		case STATE_EMPTY_FILE:
			throw new PmsiFileNotReadable("Lecteur RSF : ", new IOException("Fichier vide"));
		default:
			throw new RuntimeException("Cas non pr�vu par la machine � �tats");
		}
	}

	/**
	 * Fonction ex�cut�e lorsque la fin du flux est rencontr�e
	 */
	public void EndOfFile() throws Exception {
		changeState(SIGNAL_EOF);		
	}
	
}
