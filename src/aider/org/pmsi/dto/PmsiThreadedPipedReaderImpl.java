package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import aider.org.pmsi.dto.PmsiDtoReportError.Origin;
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
		if (sem.availablePermits() == 0 || exception != null)
			return false;
		else
			return pmsiDto.getStatus();
	}
	
	@Override
	public void run() {
		try {
			pmsiDto.writePmsi(in);
		} catch (PmsiPipedIOException e) {
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
			exception = new PmsiPipedIOException(e);
			throw (PmsiPipedIOException) exception;
		}
	}

	@Override
	public HashMap<PmsiDtoReportError, Object> getReport() {
		HashMap<PmsiDtoReportError, Object> report = pmsiDto.getReport();
		if (exception != null) {
			PmsiDtoReportError err = new PmsiDtoReportError();
			err.setOrigin(Origin.PMSI_READER);
			err.setName("TerminalException");
			report.put(err, exception);
		}
		return report;
	}
}
