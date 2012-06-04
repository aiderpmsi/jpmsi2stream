package org.aider.pmsi2sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * Permet de défini rdes options et de renvoyer la connexion à la base de données Postgresql
 * selon les arguments fournis. Les options suivantes sont définies :
 * <ul>
 * <li>Aide</li>
 * <code>-h (--help)</code>
 * <li>Numéro de version</li>
 * <code>-v (--version)</code>
 * <li>Fichier de log</li>
 * <code>-l (--logfile)</code>
 * <li>Hôte de la base de données</li>
 * <code>-dbh (--dbhost) HOST</code>
 * Définit la machine du réseau sur laquelle se trouve la base de données. Seul PostgreSQL est supporté pour le moment.
 * Par défaut, <code>localhost</code>
 * <li>Port de la base de données</li>
 * <code>-dbh (--dbhost) PORT</code>
 * Par défaut <code>5432</code>
 * <li>Utilisateur de la base de données : paramètre requis</li>
 * <code>-dbu (--dbuser) USER</code>
 * Définit l'utilisateur de la db à utiliser.
 * ATTENTION, Sera affiché dans la liste des processus, risque de sécurité  
 * <li>Mot de pass de l'utilisateur de la base de données : paramètre requis</li>
 * <code>-dbp (--dbpassword) PWD</code>
 * Définit le mot de passe de l'utilisateur de la db à utiliser.
 * ATTENTION, Trou de sécurité : sera affiché dans la liste des processus!  
 * </ul>
 * @author delabre
 *
 */
public class Pmsi2SqlBaseOptions {

	/**
	 * Définit si l'argument de demande d'affichage de l'aide a été défini
	 * dans la liste des arguments
	 */
	@Option(name = "-h", aliases = {"--help"}, usage = "Affiche l'aide")
    private boolean help;
	/**
	 * Renvoie si l'argument de demande d'affichage de l'aide a été défini
	 * dans la liste des arguments
	 * @return <code>true</code> si oui, <code>false</code> si non
	 */
    public boolean isHelp() {
    	return help;
    }

    /**
	 * Définit si l'argument de demande d'affichage de la version a été défini
	 * dans la liste des arguments
	 */
    @Option(name = "-v", aliases = {"--version"}, usage = "Affiche la version")
    private boolean version;
    /**
	 * Renvoie si l'argument de demande d'affichage de la version a été défini
	 * dans la liste des arguments
	 * @return <code>true</code> si oui, <code>false</code> si non
	 */
    public boolean isVersion() {
    	return version;
    }

    /**
     * Définit le fichier de log pour tracer les erreurs du programme (en dehors de celles
     * qui sont insérées dans la base de données)
     */
    @Option(name = "-l", aliases = {"--logfile"}, metaVar = "FILE", usage = "Specifie le fichier de trace (non utilisé)")
    private File logfile;
    /**
     * Renvoie le fichier de log utilisé pour tracer les erreurs du programme (en dehors de celles
     * qui sont insérées dans la base de données). Pour l'instant non utilisé.
     * @return {@link File} Fichier de Log
     */
    public File getLogFile() {
    	return logfile;
    }

    /**
     * Définit l'hôte de la base de données
     */
    @Option(name = "-dbh", aliases = {"--dbhost"}, metaVar = "HOST", usage = "Specifie l'hôte de la base de données")
    private String dbhost = "localhost";
    /**
     * Renvoie l'hôte de la base de données de type Postgresql (localhost par défaut)
     * @return {@link String} Nom de la base de données
     */
    public String getDbHost() {
    	return dbhost;
    }
    
    /**
     * Définit le port de la base de données 
     */
    @Option(name = "-p", aliases = {"--port"}, metaVar = "PORT", usage = "Specifie le port de la base de données")
    private int port = 5432;
    /**
     * Renvoie le port de la base de données de type Postgresql (5432 par défaut)
     * @return <code>int</code> Numéro de port
     */
    public int getDbPort() {
    	return port;
    }

    /**
     * Définit le nom de la base de données
     */
    @Option(name = "-dbn", aliases = {"--dbname"}, metaVar = "NAME", usage = "Specifie le nom de la base de données à utiliser", required=true)
    private String dbname;
    /**
     * Renvoie l'utilisateur de la base de données à utiliser
     * @return {@link String} Nom de l'utilisateur
     */
    public String getDbName() {
    	return dbname;
    }
    
    /**
     * Définit l'utilisateur de la base de données
     */
    @Option(name = "-dbu", aliases = {"--dbuser"}, metaVar = "USER", usage = "Specifie l'utilisateur de la base de données", required=true)
    private String dbuser;
    /**
     * Renvoie l'utilisateur de la base de données à utiliser
     * @return {@link String} Nom de l'utilisateur
     */
    public String getDbUser() {
    	return dbuser;
    }

    /**
     * Définit le mot de passe associé à l'utilisateur de la base de données
     */
    @Option(name = "-dbp", aliases = {"--dbpassword"}, metaVar = "PWD", usage = "Specifie le mot de passe de l'utilisateur", required=true)
    private String dbpwd;
    /**
     * Renvoie le mot de passe associé à l'utilisateur de la base de données à utiliser
     * @return {@link String} Mot de passe
     */
    public String getDbPwd() {
    	return dbpwd;
    }
    
    /**
     * Renvoie une nouvelle connexion à la base de données à partir des données
     * contenues dans les options
     * @return La nouvelle connexion
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    public Connection getNewSqlConnection() throws ClassNotFoundException, SQLException {
		// Création du lien avec la base de données
		Class.forName("org.postgresql.Driver");
		Connection myConn = DriverManager.getConnection(
				"jdbc:postgresql://" + getDbHost() + ":" +
				getDbPort() + "/" + getDbName(),
				getDbUser(), getDbPwd());
		myConn.setAutoCommit(false);
		return myConn;
    }
    
    /**
     * Liste des arguments non traités
     */
    @Argument
    private List<String> argument;
    /**
     * Renvoie la liste des arguments non traités
     * @return {@link List}<{@link String}> Arguments non traités
     */
    public List<String> getArguments() {
    	return argument;   
   }
}

