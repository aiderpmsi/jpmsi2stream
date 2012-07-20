package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import aider.org.pmsi.parser.exceptions.PmsiMuxerException;

public class PmsiStreamMuxer {

	private PipedInputStream inputStream = null;
	
	private PipedOutputStream outputStream = null;
	
	public PmsiStreamMuxer() throws PmsiMuxerException {
		inputStream = new PipedInputStream();
		outputStream = new PipedOutputStream();

		try {
			inputStream.connect(outputStream);
		} catch (IOException e) {
			this.close();
			throw new PmsiMuxerException(e);
		}
	}
	
	public PipedInputStream getInputStream() {
		return inputStream;
	}

	public PipedOutputStream getOutputStream() {
		return outputStream;
	}

	public void eof() throws PmsiMuxerException {
		try {
			outputStream.close();
			outputStream = null;
		} catch (IOException e) {
			throw new PmsiMuxerException(e);
		}
	}
	
	public boolean isClosed() throws PmsiMuxerException {
		if (inputStream == null && outputStream == null)
			return true;
		else if (inputStream != null)
			return false;
		else
			throw new PmsiMuxerException("Etats incorrects : input fermé et output ouvert");
	}
	
	public void close() throws PmsiMuxerException  {
		Exception exc1 = null, exc2 = null;
		
		try {
			if (outputStream != null)
				outputStream.close();
			outputStream = null;
		} catch (Exception e1) {
			exc1 = e1;
		}
		
		try {
			if (inputStream != null)
				inputStream.close();
			inputStream = null;
		} catch (Exception e2) {
			exc2 = e2;
		}
		
		if (exc1 == null && exc2 == null)
			return;
		else
			throw new PmsiMuxerException("Fermeture des flux impossibles");
	}
}
