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
 * Classe de base permettant deux opérations :
 * <ul>
 * <li> Création du schéma de base de données permettant d'insérer correctement les données issues d'un fichier PMSI</li>
 * <li> Insertion d'un fichier PMSI dans la base de données</li>
 * </ul>
 * Cette classe est abstraite et sert de canvas pour les classes qui réalisent réellement la lecture
 * des fichiers. Un certain nombre de fonctions doivent être surchargées.
 * @author delabre
 *
 */
public abstract class PmsiReader extends machineState {

	/**
	 * Lecteur du fichier permettant de lire des lignes.Attention, la fin de ligne est définie
	 * dans @link {@link BufferedReader#readLine()}
	 */
	private BufferedReader pmsiReader;
	
	/**
	 * Définit le signal de fin de fichier
	 */
	public static final int SIGNAL_EOF = 100;
	
	/**
	 * Stocke la dernière ligne extraite du {@link PmsiReader#pmsiReader) permettant
	 * de la traiter et de la comparer avec les lignes que l'on recherche 
	 */
	private String toParse;
	
	/**
	 * Table de hachage des types de lignes gérées par ce lecteur de fichier PMSI
	 */
	private HashMap<Integer, pmsilinetype> linesTypes;
	
	/**
	 * Stocke la connexion à la base de données
	 */
	private Connection sqlConn;
	
	/**
	 * Constructeur de la classe permettant de lire un fichier PMSI à partir d'un flux à lire et
	 * des options de connexion à la base de données
	 * @param MyReader Flux à lire. Peut être vide (StringReader("")) si on ne veut pas
	 * utiliser les fonctions de lecture de fichier (dans le cas d'utiliser uniquement les fonctions de
	 * création de schéma)
	 * @param myConn Connexion à la base de données à utiliser
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public PmsiReader(Reader MyReader, Connection myConn) throws IOException, ClassNotFoundException, SQLException {
		// Initialisation de la machine à états
		super();
		// Initialisation de la lecture du fichier à importer
		pmsiReader = new BufferedReader(MyReader);
		// Initialisation de la table de hachage des types de lignes gérées par ce lecteur de fichier PMSI
		linesTypes = new HashMap<Integer, pmsilinetype>();
		
		// Initialisation du connecteur à la base de données
		sqlConn = myConn;
	}

	/**
	 * Redéfinition de la fonction principale de la machine à état pour
	 * permettre de créer et supprimer proprement le compteur temporaire de
	 * lignes 
	 * @throws Exception 
	 */
	public void run() throws Exception {
		// Création et initialisation du compteur de lignes
		sqlConn.createStatement().execute(
				"CREATE TEMPORARY SEQUENCE line_counter START WITH 1;" +
				"SELECT nextval('line_counter');\n");
		super.run();
		sqlConn.createStatement().execute("DROP SEQUENCE line_counter;");
	}
	
	/**
	 * Lecture d'une nouvelle ligne : les données de la dernière ligne sont détruites et remplacées
	 * par les données de la ligne suivante
	 * @return La nouvelle ligne lue. 
	 * @throws Exception
	 */
	public String readNewLine() {
		try {
			sqlConn.createStatement().execute("SELECT nextval('line_counter');\n");
		} catch (SQLException e) {
			// Normalement, à ce niveau line_counter doit être défini
			throw new RuntimeException(e);
		}
		try {
			toParse = pmsiReader.readLine();
		} catch (IOException e) {
			// Normalement, à ce niveau, pmsiReader doit pouvoir être lu
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
	 * Ajout d'un type de ligne à parser
	 * @param MyLineType Permet d'identifier de manière unique le type de ligne à lire
	 * @param MyLine Définitions de la ligne à lire
	 */
	public void addLineType(int MyLineType, pmsilinetype MyLine) {
		linesTypes.put(MyLineType, MyLine);
	}
	
	/**
	 * Tentative de recherche de correspondance entre la ligne actuellement lue
	 * et les types de ligne que l'on peut rechercher
	 * @param MyLineTypes Types de lignes que l'on est susceptible de lire à ce moment
	 * @return la ligne lue, avec les données récupérées dans les différents 
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
			// Insertion des valeurs récupérées dans le type de ligne lue 
			linesTypes.get(MyIndex).setValues(MyResults);
			// Renvoi de l'objet correspondant à la ligne lue
			return linesTypes.get(MyIndex);
			}
		}
		return null;
	}
	
	/**
	 * Crée les tables dans le schéma de la base de données permettant
	 * de stocker les données issues du PMSI
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
	 * Crée les index et les contraintes d'index (unique, primary), permettant de contraindre
	 * et accélérer la base de données
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
	 * Crée les contraintes liées aux clefs étrangères
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
	 * Renvoie la connexion actuelle à la base de données
	 * @return Connection
	 */
	public Connection getSqlConnection() {
		return sqlConn;
	}
	
	/**
	 * Envoi d'un message permettant de valider les modifications réalisées
	 * sur la base de données
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		sqlConn.commit();
	}
	
	/**
	 * Fonction à appeler à la fin du fichier
	 * @throws Exception 
	 */
	abstract public void EndOfFile() throws Exception;
}
