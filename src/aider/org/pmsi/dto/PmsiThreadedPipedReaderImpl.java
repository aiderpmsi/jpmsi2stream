package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Semaphore;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

/**
 * Implémentation classique de l'interface {@link PmsiThreadedPipedReader}, écrit
 * sur la sortie standard les données récupérées
 * @author delabre
 *
 */
public class PmsiThreadedPipedReaderImpl extends PmsiThreadedPipedReader {

	/**
	 * Sémaphore utilisé pour synchroniser la lecture et l'écriture.
	 */
	private Semaphore sem;
	
	/**
	 * Flux dans lequel on lit le xml (connecté à un {@link PipedOutputStream}
	 */
	private PipedInputStream in;
	
	/**
	 * Caractérise l'état de succès ou d'échec de l'utilisation du {@link PmsiThreadedPipedReader}
	 */
	private boolean status = false;
	
	/**
	 * Stocke l'exception qui a fait échouer cette classe (si existe)
	 */
	private Exception exception = null;
	
	/**
	 * Objet gérant le transfert de données entre pmsi (un inputstream) et sa destination
	 */
	private PmsiDto pmsiDto = null;
	
	/**
	 * Création du Reader. Un semaphore est bloqué lors de l'appel du constructeur
	 * et débloqué uniquement lorsque le thread lancé par {@link PmsiThreadedPipedReaderImpl#start()}
	 * est terminé
	 * @throws PmsiPipedIOException
	 */
	public PmsiThreadedPipedReaderImpl(PmsiDto pmsiDto) throws PmsiPipedIOException {
		sem = new Semaphore(1);
		this.pmsiDto = pmsiDto;
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
			pmsiDto.writePmsi(in);
			status = true;
		} catch (PmsiPipedIOException e) {
			status = false;
			exception = e;
		} finally {
			sem.release();
		}
	}

	@Override
	public void close() throws PmsiPipedIOException {
		try {
			pmsiDto.close();
			in.close();
		} catch (IOException e) {
			throw new PmsiPipedIOException(e);
		}
	}
}
