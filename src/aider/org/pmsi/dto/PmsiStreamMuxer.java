package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import aider.org.pmsi.exceptions.PmsiMuxerException;
import aider.org.pmsi.writer.PmsiWriter;

/**
 * Classe permettant l'abstraction de deux flux de typer pipe connectés l'un à l'autre.
 * Lors de la fabrication de cette classe, un {@link PipedInputStream} et un {@link PipedInputStream}
 * sont créée puis connectés. Ces deux flux sont récupérables par {@link PmsiStreamMuxer#getInputStream()}
 * et {@link PmsiStreamMuxer#getOutputStream()}.
 * @author delabre
 *
 */
public class PmsiStreamMuxer {

	/**
	 * Pipe entrante, permettant de lire ce que le Writer a écrit
	 * @see PmsiDtoRunnable
	 */
	private PipedInputStream inputStream = null;
	
	/**
	 * Pipe sortante, permettant au Writer d'écrire à la classe de transfert de données
	 * @see PmsiWriter
	 */
	private PipedOutputStream outputStream = null;
	
	/**
	 * Constructeur, initialise les flux entrants et sortants
	 * @throws PmsiMuxerException
	 */
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
	
	/**
	 * Retourne le flux entrant
	 * @return
	 */
	public PipedInputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Retourne le flux sortant
	 * @return
	 */
	public PipedOutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Ecrit une fin de flux sur le flux entrant. Une fois cette
	 * méthode appelée, il n'est plus jamais possible de réécrire
	 * sur le flux sortant lié à cette classe
	 * @throws PmsiMuxerException
	 */
	public void eof() throws PmsiMuxerException {
		try {
			outputStream.close();
			outputStream = null;
		} catch (IOException e) {
			throw new PmsiMuxerException(e);
		}
	}
	
	/**
	 * Teste l'état du flux sortant, et vérifie leur cohérence
	 * @return
	 * @throws PmsiMuxerException si {@link PmsiStreamMuxer#inputStream} est fermé,
	 *   mais que {@link PmsiStreamMuxer#outputStream} est ouvert.
	 */
	public boolean isClosed() throws PmsiMuxerException {
		if (inputStream == null && outputStream == null)
			return true;
		else if (inputStream != null)
			return false;
		else
			throw new PmsiMuxerException("Etats incorrects : input fermé et output ouvert");
	}
	
	/**
	 * Fermeture des flux entrants et sortants, et libération de leurs ressources
	 * @throws PmsiMuxerException
	 */
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
