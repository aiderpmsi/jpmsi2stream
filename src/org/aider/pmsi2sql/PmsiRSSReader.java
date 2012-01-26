package org.aider.pmsi2sql;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.aider.pmsi2sql.linetypes.pmsilinetype;
import org.aider.pmsi2sql.linetypes.pmsirssacte;
import org.aider.pmsi2sql.linetypes.pmsirssda;
import org.aider.pmsi2sql.linetypes.pmsirssdad;
import org.aider.pmsi2sql.linetypes.pmsirssheader;
import org.aider.pmsi2sql.linetypes.pmsirssmain;

public class PmsiRSSReader extends PmsiReader {

	public static final int STATE_RSS_HEADER = 1;
	public static final int STATE_RSS_MAIN = 2;
	public static final int STATE_RSS_DA = 3;
	public static final int STATE_RSS_DAD = 4;
	public static final int STATE_RSS_ACTE = 5;
	public static final int STATE_RSS_FIN_LIGNE = 6;
	public static final int STATE_EMPTY_FILE = 7;
	
	/**
	 * SIGNAL_START : STATE_READY -> STATE_RSS_HEADER
	 */
	public static final int SIGNAL_START = 1;
	public static final int SIGNAL_RSS_END_HEADER = 6;
	public static final int SIGNAL_RSS_END_MAIN = 2;
	public static final int SIGNAL_RSS_END_DA = 3;
	public static final int SIGNAL_RSS_END_DAD = 4;
	public static final int SIGNAL_RSS_END_ACTE = 5;
	public static final int SIGNAL_RSS_NEWLINE = 6;
	
	int NbDAFaits;
	int NbDaRestants;
	int NbDADFaits;
	int NbDaDRestants;
	int NbZAFaits;
	int NbZARestants;
	
	/**
	 * Constructeur
	 * @param MyReader
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public PmsiRSSReader(Reader MyReader, Connection myConn) throws IOException, ClassNotFoundException, SQLException {
		super(MyReader, myConn);

		addLineType(STATE_RSS_HEADER, new pmsirssheader());
		addLineType(STATE_RSS_MAIN, new pmsirssmain());
		addLineType(STATE_RSS_DA, new pmsirssda());
		addLineType(STATE_RSS_DAD, new pmsirssdad());
		addLineType(STATE_RSS_ACTE, new pmsirssacte());
		
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
		
	public void process() throws PmsiFileNotReadable, PmsiFileNotInserable {
		Vector<Integer> MyV = new Vector<Integer>();
		pmsilinetype MyMatch;
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

	public void EndOfFile() throws Exception {
		changeState(SIGNAL_EOF);		
	}

	
}
