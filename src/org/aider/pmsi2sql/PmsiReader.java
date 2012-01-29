package org.aider.pmsi2sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aider.pmsi2sql.linetypes.pmsilinetype;
import org.aider.pmsi2sql.machineState.machineState;

/**
 * Classe de base permettant deux op�rations :
 * <ul>
 * <li> Cr�ation du sch�ma de base de donn�es permettant d'ins�rer correctement les donn�es issues d'un fichier PMSI</li>
 * <li> Insertion d'un fichier PMSI dans la base de donn�es</li>
 * </ul>
 * Cette classe est abstraite et sert de canvas pour les classes qui r�alisent r�ellement la lecture
 * des fichiers. Un certain nombre de fonctions doivent �tre surcharg�es.
 * @author delabre
 *
 */
public abstract class PmsiReader extends machineState {

	/**
	 * Lecteur du fichier permettant de lire des lignes.Attention, la fin de ligne est d�finie
	 * dans @link {@link BufferedReader#readLine()}
	 */
	private BufferedReader pmsiReader;
	
	/**
	 * D�finit le signal de fin de fichier
	 */
	public static final int SIGNAL_EOF = 100;
	
	/**
	 * Stocke la derni�re ligne extraite du {@link PmsiReader#pmsiReader) permettant
	 * de la traiter et de la comparer avec les lignes que l'on recherche 
	 */
	private String toParse;
	
	/**
	 * Table de hachage des types de lignes g�r�es par ce lecteur de fichier PMSI
	 */
	private HashMap<Integer, pmsilinetype> linesTypes;
	
	/**
	 * Stocke la connexion � la base de donn�es
	 */
	private Connection sqlConn;
	
	/**
	 * Constructeur de la classe permettant de lire un fichier PMSI � partir d'un flux � lire et
	 * des options de connexion � la base de donn�es
	 * @param MyReader Flux � lire. Peut �tre vide (StringReader("")) si on ne veut pas
	 * utiliser les fonctions de lecture de fichier (dans le cas d'utiliser uniquement les fonctions de
	 * cr�ation de sch�ma)
	 * @param myConn Connexion � la base de donn�es � utiliser
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public PmsiReader(Reader MyReader, Connection myConn) throws IOException, ClassNotFoundException, SQLException {
		// Initialisation de la machine � �tats
		super();
		// Initialisation de la lecture du fichier � importer
		pmsiReader = new BufferedReader(MyReader);
		// Initialisation de la table de hachage des types de lignes g�r�es par ce lecteur de fichier PMSI
		linesTypes = new HashMap<Integer, pmsilinetype>();
		
		// Initialisation du connecteur � la base de donn�es
		sqlConn = myConn;
	}

	/**
	 * Red�finition de la fonction principale de la machine � �tat pour
	 * permettre de cr�er et supprimer proprement le compteur temporaire de
	 * lignes 
	 * @throws Exception 
	 */
	public void run() throws Exception {
		// Cr�ation et initialisation du compteur de lignes
		sqlConn.createStatement().execute(
				"CREATE TEMPORARY SEQUENCE line_counter START WITH 1;" +
				"SELECT nextval('line_counter');\n");
		super.run();
		sqlConn.createStatement().execute("DROP SEQUENCE line_counter;");
	}
	
	/**
	 * Lecture d'une nouvelle ligne : les donn�es de la derni�re ligne sont d�truites et remplac�es
	 * par les donn�es de la ligne suivante
	 * @return La nouvelle ligne lue. 
	 * @throws Exception
	 */
	public String readNewLine() {
		try {
			sqlConn.createStatement().execute("SELECT nextval('line_counter');\n");
		} catch (SQLException e) {
			// Normalement, � ce niveau line_counter doit �tre d�fini
			throw new RuntimeException(e);
		}
		try {
			toParse = pmsiReader.readLine();
		} catch (IOException e) {
			// Normalement, � ce niveau, pmsiReader doit pouvoir �tre lu
			throw new RuntimeException(e);
		}
		if (toParse == null)
			changeState(SIGNAL_EOF);
		return toParse;
	}
	
	/**
	 * Retourne la ligne actuelle
	 * @return Ligne actuelle
	 */
	public String getLine() {
		return toParse;
	}
	
	/**
	 * Ajout d'un type de ligne � parser
	 * @param MyLineType Permet d'identifier de mani�re unique le type de ligne � lire
	 * @param MyLine D�finitions de la ligne � lire
	 */
	public void addLineType(int MyLineType, pmsilinetype MyLine) {
		linesTypes.put(MyLineType, MyLine);
	}
	
	/**
	 * Tentative de recherche de correspondance entre la ligne actuellement lue
	 * et les types de ligne que l'on peut rechercher
	 * @param MyLineTypes Types de lignes que l'on est susceptible de lire � ce moment
	 * @return la ligne lue, avec les donn�es r�cup�r�es dans les diff�rents 
	 */
	public pmsilinetype parseLine(Vector<Integer> MyLineTypes) {
		Iterator<Integer> it = MyLineTypes.iterator();
		while (it.hasNext()) {
			int MyIndex = it.next();
			Pattern MyRegex = linesTypes.get(MyIndex).getRegex();
			Matcher MyMatch = MyRegex.matcher(getLine());
			if (MyMatch.matches()) {
				Vector<String> MyResults = new Vector<String>(); 
				for (int MyI = 0 ; MyI < MyMatch.groupCount(); MyI++) {
					MyResults.add(MyMatch.group(MyI + 1));
				}
			// Insertion des valeurs r�cup�r�es dans le type de ligne lue 
			linesTypes.get(MyIndex).setValues(MyResults);
			// Renvoi de l'objet correspondant � la ligne lue
			return linesTypes.get(MyIndex);
			}
		}
		return null;
	}
	
	/**
	 * Cr�e les tables dans le sch�ma de la base de donn�es permettant
	 * de stocker les donn�es issues du PMSI
	 * @throws SQLException
	 */
	public void createTables() throws SQLException {
		Set<Integer> t = linesTypes.keySet();
		Iterator<Integer> u = t.iterator();
		
		while(u.hasNext()) {
			sqlConn.createStatement().execute(linesTypes.get(u.next()).getSQLTable());
		}
	}
	
	/**
	 * Cr�e les index et les contraintes d'index (unique, primary), permettant de contraindre
	 * et acc�l�rer la base de donn�es
	 * @throws SQLException
	 */
	public void createIndexes() throws SQLException {
		Set<Integer> t = linesTypes.keySet();
		Iterator<Integer> u = t.iterator();
		
		while(u.hasNext()) {
			sqlConn.createStatement().execute(linesTypes.get(u.next()).getSQLIndex());
		}
	}
	
	/**
	 * Cr�e les contraintes li�es aux clefs �trang�res
	 * @throws SQLException
	 */
	public void createKF() throws SQLException {
		Set<Integer> t = linesTypes.keySet();
		Iterator<Integer> u = t.iterator();
		
		while(u.hasNext()) {
			sqlConn.createStatement().execute(linesTypes.get(u.next()).getSQLFK());
		}
	}
	
	/**
	 * Suppression du contenu du buffer stockant la ligne actuelle
	 */
	public void flush_line() {
		toParse = new String();
	}

	/**
	 * Renvoie la connexion actuelle � la base de donn�es
	 * @return Connection
	 */
	public Connection getSqlConnection() {
		return sqlConn;
	}
	
	/**
	 * Envoi d'un message permettant de valider les modifications r�alis�es
	 * sur la base de donn�es
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		sqlConn.commit();
	}
	
	/**
	 * Fonction � appeler � la fin du fichier
	 * @throws Exception 
	 */
	abstract public void EndOfFile() throws Exception;
}
