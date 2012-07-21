package aider.org.pmsi.dto;

import aider.org.pmsi.parser.exceptions.PmsiRunnableException;

public interface PmsiRunnable {

	public void run() throws PmsiRunnableException;
	
	public void close() throws PmsiRunnableException;
}
