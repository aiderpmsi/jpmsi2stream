package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.concurrent.Semaphore;

/**
 * Classe permettant de lire le flux écrit par le thread principal pour l'écrire où il
 * elle le veut
 * @author delabre
 *
 */
public class PmsiPipedReaderImpl extends PmsiPipedReader {

	/**
	 * Sémaphore utilisé pour synchroniser la lecture et l'écriture.
	 */
	private Semaphore sem;
	
	/**
	 * Flux dans lequel on lit le xml
	 */
	private PipedInputStream in;
	
	/**
	 * Caractérise l'état de succès de la lecture ou d'échec
	 */
	private boolean status = false;
	
	/**
	 * Stocke l'exception qui a fait échouer cette classe si existe
	 */
	private Exception exception;
	
	/**
	 * Crée l'objet d'écriture du flux
	 * lecture et écriture
	 * @throws PmsiPipedIOException
	 */
	public PmsiPipedReaderImpl() throws PmsiPipedIOException {
		in = new PipedInputStream(); 
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
	public PipedInputStream getPipedInputStream() {
		return in;
	}
	
	@Override
	public boolean getStatus() {
		return status;
	}
	
	@Override
	public Exception getTerminalException() {
		return exception;
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
	 * Ecrit les données de l'inputstream là où c'est nécessaire
	 * @throws PmsiPipedIOException 
	 */
	private void writeInputStream(InputStream input) throws PmsiPipedIOException {
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
