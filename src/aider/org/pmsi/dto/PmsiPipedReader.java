package aider.org.pmsi.dto;

import java.io.PipedOutputStream;
import java.util.concurrent.Semaphore;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Classe avec 2 fonctions :
 * <ul>
 *  <li>étend {@link Thread} pour créer un processus distinct qui va lire le flux généré
 *  par {@link PmsiPipedWriter}</li>
 *  <li>réalise le stockage du flux (base de données sql, xml, fichier, ...)
 * @author delabre
 *
 */
public abstract class PmsiPipedReader extends Thread {

	/**
	 * Méthode de fonctionnement du thread. Sa définition ou sa surcharge permet
	 * de définir un autre mode de stockage
	 */
	public abstract void run();

	/**
	 * Récupère le sémaphore utilisé par le {@link PmsiPipedReader}. Il est bloqué à la
	 * création de la classe et débloqué lorsqu'il a fini de lire le flux du pipedwriter
	 * @return
	 * @throws PmsiPipedIOException
	 */
	public abstract Semaphore getSemaphore() throws PmsiPipedIOException;
	
	/**
	 * Connecte ce {@link PmsiPipedReader} à un flux de type {@link PipedOutputStream}
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
	 */
	public abstract void close() throws PmsiPipedIOException;
}
