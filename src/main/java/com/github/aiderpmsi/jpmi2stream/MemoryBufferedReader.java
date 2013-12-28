package com.github.aiderpmsi.jpmi2stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MemoryBufferedReader extends BufferedReader {

	private String readed = "";
	
	public MemoryBufferedReader(Reader in) {
		super(in);
	}

	public MemoryBufferedReader(Reader in, int sz) {
		super(in, sz);
	}

	public String getLine() throws IOException {
		if (readed == null || readed.length() == 0) {
			readed = readLine();
		}
		return readed;
	}
	
	/**
	 * Supprime un nombre d'éléments de la ligne actuellement lue.
	 * Si le nombre d'éléments à supprimer est plus important que la taille de la ligne,
	 * seule la ligne actuelle est effacée, pas les lignes suivantes
	 * @param nbElts
	 */
	public void consume(int nbElts) {
		readed = readed.substring(nbElts);
	}
	
}
