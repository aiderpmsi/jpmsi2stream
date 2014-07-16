package com.github.aiderpmsi.pims.parser.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class PimsParserFromReader extends BufferedReader implements PimsParserSupplier {

	private String readed = "";
	
	public PimsParserFromReader(Reader in) {
		super(in);
	}

	public PimsParserFromReader(Reader in, int sz) {
		super(in, sz);
	}

	@Override
	public String readLine() throws IOException {
		if (readed == null || readed.length() == 0) {
			readed = super.readLine();
		}
		return readed;
	}
	
	/**
	 * Supprime un nombre d'éléments de la ligne actuellement lue.
	 * Si le nombre d'éléments à supprimer est plus important que la taille de la ligne,
	 * seule la ligne actuelle est effacée, pas les lignes suivantes
	 * @param nbElts
	 */
	@Override
	public void consume(int nbElts) {
		if (readed != null)
			readed = readed.substring(nbElts);
	}
	
}
