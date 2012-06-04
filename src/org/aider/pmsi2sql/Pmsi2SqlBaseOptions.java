package org.aider.pmsi2sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * Permet de d�fini rdes options et de renvoyer la connexion � la base de donn�es Postgresql
 * selon les arguments fournis. Les options suivantes sont d�finies :
 * <ul>
 * <li>Aide</li>
 * <code>-h (--help)</code>
 * <li>Num�ro de version</li>
 * <code>-v (--version)</code>
 * <li>Fichier de log</li>
 * <code>-l (--logfile)</code>
 * <li>H�te de la base de donn�es</li>
 * <code>-dbh (--dbhost) HOST</code>
 * D�finit la machine du r�seau sur laquelle se trouve la base de donn�es. Seul PostgreSQL est support� pour le moment.
 * Par d�faut, <code>localhost</code>
 * <li>Port de la base de donn�es</li>
 * <code>-dbh (--dbhost) PORT</code>
 * Par d�faut <code>5432</code>
 * <li>Utilisateur de la base de donn�es : param�tre requis</li>
 * <code>-dbu (--dbuser) USER</code>
 * D�finit l'utilisateur de la db � utiliser.
 * ATTENTION, Sera affich� dans la liste des processus, risque de s�curit�  
 * <li>Mot de pass de l'utilisateur de la base de donn�es : param�tre requis</li>
 * <code>-dbp (--dbpassword) PWD</code>
 * D�finit le mot de passe de l'utilisateur de la db � utiliser.
 * ATTENTION, Trou de s�curit� : sera affich� dans la liste des processus!  
 * </ul>
 * @author delabre
 *
 */
public class Pmsi2SqlBaseOptions {

	/**
	 * D�finit si l'argument de demande d'affichage de l'aide a �t� d�fini
	 * dans la liste des arguments
	 */
	@Option(name = "-h", aliases = {"--help"}, usage = "Affiche l'aide")
    private boolean help;
	/**
	 * Renvoie si l'argument de demande d'affichage de l'aide a �t� d�fini
	 * dans la liste des arguments
	 * @return <code>true</code> si oui, <code>false</code> si non
	 */
    public boolean isHelp() {
    	return help;
    }

    /**
	 * D�finit si l'argument de demande d'affichage de la version a �t� d�fini
	 * dans la liste des arguments
	 */
    @Option(name = "-v", aliases = {"--version"}, usage = "Affiche la version")
    private boolean version;
    /**
	 * Renvoie si l'argument de demande d'affichage de la version a �t� d�fini
	 * dans la liste des arguments
	 * @return <code>true</code> si oui, <code>false</code> si non
	 */
    public boolean isVersion() {
    	return version;
    }

    /**
     * D�finit le fichier de log pour tracer les erreurs du programme (en dehors de celles
     * qui sont ins�r�es dans la base de donn�es)
     */
    @Option(name = "-l", aliases = {"--logfile"}, metaVar = "FILE", usage = "Specifie le fichier de trace (non utilis�)")
    private File logfile;
    /**
     * Renvoie le fichier de log utilis� pour tracer les erreurs du programme (en dehors de celles
     * qui sont ins�r�es dans la base de donn�es). Pour l'instant non utilis�.
     * @return {@link File} Fichier de Log
     */
    public File getLogFile() {
    	return logfile;
    }

    /**
     * D�finit l'h�te de la base de donn�es
     */
    @Option(name = "-dbh", aliases = {"--dbhost"}, metaVar = "HOST", usage = "Specifie l'h�te de la base de donn�es")
    private String dbhost = "localhost";
    /**
     * Renvoie l'h�te de la base de donn�es de type Postgresql (localhost par d�faut)
     * @return {@link String} Nom de la base de donn�es
     */
    public String getDbHost() {
    	return dbhost;
    }
    
    /**
     * D�finit le port de la base de donn�es 
     */
    @Option(name = "-p", aliases = {"--port"}, metaVar = "PORT", usage = "Specifie le port de la base de donn�es")
    private int port = 5432;
    /**
     * Renvoie le port de la base de donn�es de type Postgresql (5432 par d�faut)
     * @return <code>int</code> Num�ro de port
     */
    public int getDbPort() {
    	return port;
    }

    /**
     * D�finit le nom de la base de donn�es
     */
    @Option(name = "-dbn", aliases = {"--dbname"}, metaVar = "NAME", usage = "Specifie le nom de la base de donn�es � utiliser", required=true)
    private String dbname;
    /**
     * Renvoie l'utilisateur de la base de donn�es � utiliser
     * @return {@link String} Nom de l'utilisateur
     */
    public String getDbName() {
    	return dbname;
    }
    
    /**
     * D�finit l'utilisateur de la base de donn�es
     */
    @Option(name = "-dbu", aliases = {"--dbuser"}, metaVar = "USER", usage = "Specifie l'utilisateur de la base de donn�es", required=true)
    private String dbuser;
    /**
     * Renvoie l'utilisateur de la base de donn�es � utiliser
     * @return {@link String} Nom de l'utilisateur
     */
    public String getDbUser() {
    	return dbuser;
    }

    /**
     * D�finit le mot de passe associ� � l'utilisateur de la base de donn�es
     */
    @Option(name = "-dbp", aliases = {"--dbpassword"}, metaVar = "PWD", usage = "Specifie le mot de passe de l'utilisateur", required=true)
    private String dbpwd;
    /**
     * Renvoie le mot de passe associ� � l'utilisateur de la base de donn�es � utiliser
     * @return {@link String} Mot de passe
     */
    public String getDbPwd() {
    	return dbpwd;
    }
    
    /**
     * Renvoie une nouvelle connexion � la base de donn�es � partir des donn�es
     * contenues dans les options
     * @return La nouvelle connexion
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    public Connection getNewSqlConnection() throws ClassNotFoundException, SQLException {
		// Cr�ation du lien avec la base de donn�es
		Class.forName("org.postgresql.Driver");
		Connection myConn = DriverManager.getConnection(
				"jdbc:postgresql://" + getDbHost() + ":" +
				getDbPort() + "/" + getDbName(),
				getDbUser(), getDbPwd());
		myConn.setAutoCommit(false);
		return myConn;
    }
    
    /**
     * Liste des arguments non trait�s
     */
    @Argument
    private List<String> argument;
    /**
     * Renvoie la liste des arguments non trait�s
     * @return {@link List}<{@link String}> Arguments non trait�s
     */
    public List<String> getArguments() {
    	return argument;   
   }
}

