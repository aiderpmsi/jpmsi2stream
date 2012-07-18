package aider.org.pmsi.dto;

import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Semaphore;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Classe avec 2 fonctions :
 * <ul>
 *  <li>étend {@link Thread} pour créer un processus distinct qui va lire le flux généré
 *  par {@link PmsiPipedWriter}</li>
 *  <li>réalise le stockage du flux (base de données sql, xml, fichier, ...) grâce au
 *  {@link PmsiDto} donné en construction</li>
 * </ul>
 * Il faut donc :
 * <ol>
 *  <li>Créer la classe avec le constructeur pour lier le {@link PmsiDto} associé</li>
 *  <li>Connecter le {@link PmsiThreadedPipedReader} avec un {@link OutputStream} par
 *  {@link PmsiThreadedPipedReader#connect(PipedOutputStream)}</li>
 *  <li>Lancer le {@link PmsiThreadedPipedReader} avec {@link PmsiThreadedPipedReader#start()}
 *  <li>Ecrire sur le {@link OutputStream} les données nécessaires puis le fermer</li>
 *  <li>Attendre que le semaphore {@link PmsiThreadedPipedReader#getSemaphore()} soit débloqué,
 *  indiquant que le management de l'{@link OutputStream} est fini</li>
 *  <li>Finir par libérer les resources de cet objet : {@link PmsiThreadedPipedReader#close()}</li>
 * </ol>
 * @author delabre
 *
 */
public abstract class PmsiThreadedPipedReader extends Thread {

	/**
	 * Méthode de fonctionnement du thread. Sa définition ou sa surcharge permet
	 * de définir un autre mode de stockage
	 */
	public abstract void run();

	/**
	 * Récupère le sémaphore utilisé par le {@link PmsiThreadedPipedReader}. Il est bloqué à la
	 * création de la classe et débloqué lorsqu'il a fini de lire le flux du pipedwriter
	 * @return sémaphore utilisé pour attendre la fin du reader
	 * @throws PmsiPipedIOException
	 */
	public abstract Semaphore getSemaphore() throws PmsiPipedIOException;
	
	/**
	 * Connecte ce {@link PmsiThreadedPipedReader} à un flux de type {@link PipedOutputStream}
	 * @param out
	 * @throws PmsiPipedIOException 
	 */
	public abstract void connect(PipedOutputStream out) throws PmsiPipedIOException;
	
	/**
	 * Récupère le statut d'écriture du flux
	 * @return <code>true</code> si le flux a bien pu être écrit, <code>false</code> sinon
	 */
	public abstract boolean getStatus();
	
	/**
	 * Récupère l'erreur qui a terminé le thread si le thread a été arrêté par une exception
	 * @return l'erreur, ou null si aucune
	 */
	public abstract Exception getTerminalException();
	
	/**
	 * Libère l'ensemble des resources liées à cette classe
	 * @throws PmsiPipedIOException
	 */
	public abstract void close() throws PmsiPipedIOException;
}
