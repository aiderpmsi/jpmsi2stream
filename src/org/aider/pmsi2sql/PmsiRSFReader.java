package org.aider.pmsi2sql;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.aider.pmsi2sql.linetypes.pmsilinetype;
import org.aider.pmsi2sql.linetypes.pmsirsfa;
import org.aider.pmsi2sql.linetypes.pmsirsfb;
import org.aider.pmsi2sql.linetypes.pmsirsfc;
import org.aider.pmsi2sql.linetypes.pmsirsfh;
import org.aider.pmsi2sql.linetypes.pmsirsfheader;
import org.aider.pmsi2sql.linetypes.pmsirsfm;


public class PmsiRSFReader extends PmsiReader {

	public static final int STATE_RSF_HEADER = 1;
	public static final int STATE_RSF_LINES = 2;
	
	/**
	 * SIGNAL_START : STATE_READY -> STATE_RSS_HEADER
	 */
	public static final int SIGNAL_START = 1;
	public static final int SIGNAL_RSF_END_HEADER = 2;
	public static final int SIGNAL_RSF_END_LINES = 3;
	public static final int STATE_EMPTY_FILE = 4;
	
	public static final int RSFHEADER = 1;
	public static final int RSFA = 2;
	public static final int RSFB = 3;
	public static final int RSFC = 4;
	public static final int RSFH = 5;
	public static final int RSFM = 6;
		
	/**
	 * Constructeur
	 * @param MyReader
	 * @param myConn
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public PmsiRSFReader(Reader MyReader, Connection myConn) throws IOException, ClassNotFoundException, SQLException {
		super(MyReader, myConn);

		addLineType(RSFHEADER, new pmsirsfheader());
		addLineType(RSFA, new pmsirsfa());
		addLineType(RSFB, new pmsirsfb());
		addLineType(RSFC, new pmsirsfc());
		addLineType(RSFH, new pmsirsfh());
		addLineType(RSFM, new pmsirsfm());
		
		addTransition(SIGNAL_START, STATE_READY, STATE_RSF_HEADER);
		addTransition(SIGNAL_EOF, STATE_RSF_HEADER, STATE_EMPTY_FILE);
		addTransition(SIGNAL_RSF_END_HEADER, STATE_RSF_HEADER, STATE_RSF_LINES);
		addTransition(SIGNAL_RSF_END_LINES, STATE_RSF_LINES, STATE_RSF_LINES);
		addTransition(SIGNAL_EOF, STATE_RSF_LINES, STATE_FINISHED);
	}
	
	/**
	 * @throws PmsiFileNotInserable 
	 * 
	 */
	public void process() throws PmsiFileNotReadable, PmsiFileNotInserable {
		Vector<Integer> MyV = new Vector<Integer>();
		pmsilinetype MyMatch;
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
					throw new PmsiFileNotInserable("Lecteur entête RSF : ", e);
				}
				changeState(SIGNAL_RSF_END_HEADER);
				readNewLine();
			} else
				throw new PmsiFileNotReadable("Lecteur RSF : Entête du fichier non trouvé");
			break;
		case STATE_RSF_LINES:
			MyV.add(RSFA);
			MyV.add(RSFB);
			MyV.add(RSFC);
			MyV.add(RSFH);
			MyV.add(RSFM);
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
				// Une ligne inconnue peut être lié à un nouveau format, c'est donc pas
				// une erreur qui empêche d'insérer le fichier
				throw new PmsiFileNotReadable("Lecteur RSF : Ligne inconnue");
			}
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
