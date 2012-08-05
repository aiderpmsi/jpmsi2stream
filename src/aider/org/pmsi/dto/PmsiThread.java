package aider.org.pmsi.dto;

import java.util.concurrent.Semaphore;

import aider.org.pmsi.exceptions.PmsiException;
import aider.org.pmsi.exceptions.PmsiDtoRunnableException;
import aider.org.pmsi.writer.PmsiWriter;

/**
 * Classe permettant de lancer un {@link PmsiDtoRunnable}, de recenser les erreurs éventuelles
 * qui y ont eu lieu, et de synchroniser les processus de lecture et écriture sur les flux
 * entrants et sortants de {@link PmsiStreamMuxer}
 * @author delabre
 *
 */
public class PmsiThread extends Thread {

	/**
	 * Exception éventuellement relevée lors du lancement de {@link PmsiThread#runnable}
	 */
	private PmsiException exception = null;
	
	/**
	 * Classe implémentant {@link PmsiDtoRunnable} à lancer
	 */
	private PmsiDtoRunnable runnable;
	
	/**
	 * Sémaphore garantissant à {@link PmsiThread#waitEndOfProcess()} la possibilité au thread principal
	 * exécutant {@link PmsiWriter} d'attendre le thread secondaire exécutant {@link PmsiThread#runnable}
	 */
	private Semaphore semaphore = new Semaphore(1);
	
	/**
	 * Constructeur
	 * @param runnable sera lancée lors de l'exécution de {@link PmsiThread#runnable}
	 */
	public PmsiThread(PmsiDtoRunnable runnable) {
		this.runnable = runnable;
		try {
			semaphore.acquire();
		} catch (InterruptedException ignore) {
			// Ignorer une demande d'interruption ici
			// Il ne peut jamais y avoir de bloquage
		}
	}
	
	/**
	 * Exécute en parallèle dans un thread secondaire la méthode {@link PmsiDtoRunnable#run()}
	 * de {@link PmsiThread#runnable}
	 */
	public void run() {
		try {
			runnable.run();
		} catch (PmsiDtoRunnableException e) {
			exception = e;
		} finally {
			semaphore.release();
		}
	}
	
	/**
	 * Renvoie une éventuelle exception lancée lors de l'exécution de {@link PmsiThread#runnable}
	 * @return
	 */
	public PmsiException getTerminalException() {
		return exception;
	}

	/**
	 * Attend (de manière bloquante) la fin de l'exécution de {@link PmsiThread#runnable}
	 * @throws InterruptedException
	 */
	public void waitEndOfProcess() throws InterruptedException {
		semaphore.acquire();
		semaphore.release();
	}
	
}
