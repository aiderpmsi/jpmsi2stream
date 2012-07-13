package aider.org.pmsi.dto;

import java.io.InputStream;
import java.util.concurrent.Semaphore;

/**
 * Classe permettant de lire le flux écrit par le thread principal pour l'écrire où il
 * elle le veut
 * @author delabre
 *
 */
public class DtoPmsiWriter extends Thread {

	/**
	 * Sémaphore utilisé pour synchroniser la lecture et l'écriture. Il est partagé
	 * entre {@link DtoPmsiImpl} et {@link DtoPmsiWriter}
	 */
	private Semaphore sem;
	
	/**
	 * Flux dans lequel on lit le xml
	 */
	private InputStream in;
	
	/**
	 * Caractérise l'état de succès de la lecture ou d'échec
	 */
	private boolean status = false;
	
	/**
	 * Stocke l'exception qui a fait échouer cette classe si existe
	 */
	private Exception exception;
	
	/**
	 * Crée l'objet d'écriture du flux in, avec le semaphore sem permettant de synchroniser
	 * lecture et écriture
	 * @param sem
	 * @param in
	 * @throws DtoPmsiException 
	 */
	public DtoPmsiWriter(Semaphore sem, InputStream in) throws DtoPmsiException {
		this.sem = sem;
		this.in = in;
		// On bloque le sémaphore ici. Il sera débloqué uniquement lorsque l'écriture de DtoPmsiWriter
		// aura été terminée. Comme on cherchera à le bloquer lorsqu'on aura fini la
		// lecture dans la classe DtoPmsiImpl, on ne pourra fermer les flux que lorsque le dtoPmsiWriter aura fini
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			throw new DtoPmsiException(e);
		}
	}
	
	/**
	 * Méthode de fonctionnement du thread
	 */
	public void run() {
		try {
			writeInputStream();
			status = true;
		} catch (DtoPmsiException e) {
			status = false;
			exception = e;
		} finally {
			sem.release();
		}
	}
	
	/**
	 * Récupère le statut d'écriture du flux
	 * @return <code>true</code> si le flux a bien pu être écrit, <code>false</code> sinon
	 */
	public boolean getStatus() {
		return status;
	}
	
	public Exception getException() {
		return exception;
	}
	
	/**
	 * Ecrit les données de l'inputstream là où c'est nécessaire
	 * (peut être surchargé pour écrire dans une base de données)
	 * @throws DtoPmsiException 
	 */
	private void writeInputStream() throws DtoPmsiException {
		try {
			byte buffer[] = new byte[512];
			int size;
			while ((size = in.read(buffer)) != -1) {
				System.out.println(new String(buffer, 0, size));
				// Vérification que le thread n'a pas été interrompu
				if (Thread.currentThread().isInterrupted())
					throw new InterruptedException("Thread interrompu");
			}
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}
}
