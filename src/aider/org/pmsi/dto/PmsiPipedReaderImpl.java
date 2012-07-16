package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Semaphore;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Implémentation classique de l'interface {@link PmsiPipedReader}, écrit
 * sur la sortie standard les données récupérées
 * @author delabre
 *
 */
public class PmsiPipedReaderImpl extends PmsiPipedReader {

	/**
	 * Sémaphore utilisé pour synchroniser la lecture et l'écriture.
	 */
	private Semaphore sem;
	
	/**
	 * Flux dans lequel on lit le xml (connecté à un {@link PipedOutputStream}
	 */
	private PipedInputStream in;
	
	/**
	 * Caractérise l'état de succès ou d'échec de l'utilisation du {@link PmsiPipedReader}
	 */
	private boolean status = false;
	
	/**
	 * Stocke l'exception qui a fait échouer cette classe (si existe)
	 */
	private Exception exception = null;
	
	/**
	 * Création du Reader. Un semaphore est bloqué lors de l'appel du constructeur
	 * et débloqué uniquement lorsque le thread lancé par {@link PmsiPipedReaderImpl#start()}
	 * est terminé
	 * @throws PmsiPipedIOException
	 */
	public PmsiPipedReaderImpl() throws PmsiPipedIOException {
		sem = new Semaphore(1);
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			throw new PmsiPipedIOException(e);
		}
	}
	
	@Override
	public Semaphore getSemaphore() throws PmsiPipedIOException {
		return sem;
	}
	
	@Override
	public void connect(PipedOutputStream out) throws PmsiPipedIOException  {
		try {
			in = new PipedInputStream(out);
		} catch (IOException e) {
			throw new PmsiPipedIOException(e);
		}
	}
	
	@Override
	public boolean getStatus() {
		return status;
	}
	
	/**
	 * Permet de définir le statut (réussi ou échoué) de l'écriture du reader
	 * @param status
	 */
	protected void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	public Exception getTerminalException() {
		return exception;
	}

	/**
	 * Permet de définir l'exception terminale d'écriture des données du reader
	 * @param e
	 */
	protected void setTerminalException(Exception e) {
		exception = e;
	}
	
	@Override
	public void run() {
		try {
			writeInputStream(in);
			status = true;
		} catch (PmsiPipedIOException e) {
			status = false;
			exception = e;
		} finally {
			sem.release();
		}
	}
	
	/**
	 * Ecrit les données de l'inputstream sur la sortie standard
	 * @throws PmsiPipedIOException 
	 */
	protected void writeInputStream(InputStream input) throws PmsiPipedIOException {
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
			throw new PmsiPipedIOException(e);
		}
	}

	@Override
	public void close() throws PmsiPipedIOException {
		try {
			in.close();
		} catch (IOException e) {
			throw new PmsiPipedIOException(e);
		}
	}
}
