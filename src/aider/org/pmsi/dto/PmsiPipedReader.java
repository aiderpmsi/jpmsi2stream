package aider.org.pmsi.dto;

import java.io.PipedInputStream;
import java.util.concurrent.Semaphore;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Classe permettant de lire le flux écrit par le thread principal pour l'écrire où il
 * elle le veut
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
	 * Récupère le sémaphore utilisé par le pipedreader 
	 * @param sem
	 * @throws PmsiPipedIOException
	 */
	public abstract Semaphore getSemaphore() throws PmsiPipedIOException;
	
	/**
	 * Récupère le flux qui est généré et lu par le pipedreader
	 * @param in
	 */
	public abstract PipedInputStream getPipedInputStream();
	
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
