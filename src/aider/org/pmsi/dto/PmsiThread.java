package aider.org.pmsi.dto;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import aider.org.pmsi.parser.exceptions.PmsiException;
import aider.org.pmsi.parser.exceptions.PmsiRunnableException;

public class PmsiThread extends Thread {

	private Queue<PmsiException> exceptions = new LinkedList<PmsiException>();

	private PmsiRunnable runnable;
	
	private Semaphore semaphore = new Semaphore(1);
	
	public PmsiThread(PmsiRunnable runnable) {
		this.runnable = runnable;
		try {
			semaphore.acquire();
		} catch (InterruptedException ignore) {
			// Ignorer une demande d'interruption ici
			// Il ne peut jamais y avoir de bloquage
		}
	}
	
	public void run() {
		try {
			runnable.run();
		} catch (PmsiRunnableException e) {
			exceptions.add(e);
		} finally {
			semaphore.release();
		}
	}
	
	public Exception pollTerminalException() {
		return exceptions.poll();
	}
	
	public Exception getTerminalException() {
		return exceptions.peek();
	}
	
	public void waitEndOfProcess() throws InterruptedException {
		semaphore.acquire();
		semaphore.release();
	}
	
}
