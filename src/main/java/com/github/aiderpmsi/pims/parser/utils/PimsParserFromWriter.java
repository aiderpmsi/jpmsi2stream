package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.io.Writer;

public class PimsParserFromWriter extends Writer implements PimsParserSupplier {

	private final StringBuffer currentLine = new StringBuffer();
	
	private final StringBuffer buffer = new StringBuffer();
	
	private int nbBufferedLines = 0;
	
	@Override
	public synchronized String readLine() throws IOException {
		if (currentLine.length() != 0) {
			// CURRENT LINE EXISTS, RETURN IT
			return currentLine.toString();
		} else if (nbBufferedLines != 0) {
			// GET THE OCCURENCE OF A NEWLINE
			int newLinePos = buffer.indexOf("\n");
			if (newLinePos == -1) {
				// IF nbBufferedLines != 0 AND WE DON'T HAVE A NEWLINE, IT MEANS WE ARE AT EOF
				if (buffer.length() != 0) {
					currentLine.append(buffer);
					buffer.delete(0, buffer.length());
					return currentLine.toString();
				} else {
					return null;
				}
			} else {
				// WE HAVE A CLASSIC NEWLINE
				// COPY UNTIL NEWLINE
				char[] newLineChars = new char[newLinePos];
				buffer.getChars(0, newLinePos, newLineChars, 0);
				// FILL CurrentLine
				currentLine.append(newLineChars);
				// REMOVE TERMINATING \r IF EXIST (WINDOWS FILE)
				if (currentLine.charAt(currentLine.length() - 1) == '\r')
					currentLine.deleteCharAt(currentLine.length() - 1);
				// REMOVE CURRENT LINE FROM BUFFER
				buffer.delete(0, newLinePos + 1);
				nbBufferedLines -= 1;
				// PREVENT THAT WE CHANGED nbBufferedLines
				notifyAll();
				return currentLine.toString();
			}
		} else {
			// BLOCKS WHILE nbBufferedLines EQUALS 0
			while (nbBufferedLines == 0) {
				notifyAll();
				try {
					wait();
				} catch (InterruptedException e) {
					throw new IOException(e);
				}
			}
			// nbBufferedLines IS DIFERENT THAN ZERO, RETURN A LINE
			return readLine();
		}
	}

	@Override
	public synchronized void consume(int nbElts) throws IOException {
		currentLine.delete(0, nbElts > currentLine.length() ? currentLine.length() : nbElts);
	}

	@Override
	public synchronized void write(char[] cbuf, int off, int len) throws IOException {
		// WRITES A NUMBER OF CHARS AND BLOCKS IF WE HAVE A NEWLINE CHAR IN LINE
		buffer.append(cbuf, off, len);

		// COUNTS THE NUMBER OF NEWLINES
		for (int i = off ; i < off + len ; i++) {
			if (cbuf[i] == '\n')
				nbBufferedLines += 1;
		}

		// NOW WAIT WHILE nbBufferedLines GREATER THAN 0 (Object.Wait() RELEASES THE SYNCHRO
		while (nbBufferedLines != 0) {
			// NOTIFY THAT WE HAVE REMAINING LINES
			notifyAll();
			try {
				wait();
			} catch (InterruptedException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public void flush() throws IOException {
		// NOTHING TO DO
	}

	@Override
	public synchronized void close() throws IOException {
		// ADD ONE 'PSEUDO' NEW LINE, IN ORDER TO KNOW THAT WHEN WE DON'T HAVE A NEWLINE WITH nbBufferedLines > 0 THAT
		// WE ARE AT EOF
		nbBufferedLines +=1;
	}

}
