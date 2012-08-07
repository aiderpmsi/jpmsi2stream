package aider.org.pmsi.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import aider.org.machinestate.MachineState;
import aider.org.pmsi.exceptions.PmsiReaderException;
import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

/**
 * Classe de base de parseur pmsi.
 * @author delabre
 *
 * @param <EnumState>
 * @param <EnumSignal>
 */
public abstract class PmsiParser<EnumState, EnumSignal> extends MachineState<EnumState, EnumSignal, String> {

	/**
	 * Lecteur du fichier permettant de lire des lignes.Attention, la fin de ligne est définie
	 * dans @link {@link BufferedReader#readLine()}
	 */
	private BufferedReader reader;
	
	/**
	 * Stocke la dernière ligne extraite du {@link PmsiParser#pmsiReader) permettant
	 * de la traiter et de la comparer avec les lignes que l'on recherche 
	 */
	private String toParse;
		
	/**
	 * Table de hachage des types de lignes gérées par ce lecteur de fichier PMSI
	 */
	private HashMap<EnumState, List<PmsiLineType>> linesTypes = new HashMap<EnumState, List<PmsiLineType>>();

	protected PmsiParser() { }
	
	/**
	 * Constructeur de la classe permettant de lire un fichier PMSI à partir d'un flux
	 * et d'états définissant la manière de lire un fichier
	 * @param reader Flux à lire.
	 * @param stateReady
	 * @param stateFinished
	 */
	public PmsiParser(
			Reader reader,
			EnumState stateReady,
			EnumState stateFinished) {
		// Initialisation de la machine à états
		super(stateReady, stateFinished);

		// Initialisation de la lecture du fichier à importer
		this.reader = new BufferedReader(reader);
	}
	
	/**
	 * Lecture d'une nouvelle ligne : les données de la dernière ligne sont détruites et remplacées
	 * par les données de la ligne suivante
	 * @throws Exception
	 */
	public void readNewLine() throws PmsiReaderException {
		try {
			toParse = reader.readLine();
		} catch (IOException e) {
			throw new PmsiReaderException(e);
		}
		
		if (toParse == null)
			endOfFile();
	}
	
	/**
	 * Retourne la taille de la ligne actuelle
	 * @return Ligne actuelle
	 */
	public final int getLineSize() {
		return toParse.length();
	}
	
	/**
	 * Ajout d'un type de ligne à parser
	 * @param state état permettant de lire la ligne
	 * @param pmsiLine Définitions de la ligne à lire
	 */
	public void addLineType(EnumState state, PmsiLineType pmsiLine) {
		if (linesTypes.get(state) == null)
			linesTypes.put(state, new ArrayList<PmsiLineType>());
		
		linesTypes.get(state).add(pmsiLine);
	}
	
	/**
	 * Tentative de recherche de correspondance entre la ligne actuellement lue
	 * et les types de ligne que l'on peut rechercher
	 * @return la ligne lue, avec les données récupérées dans le contenu ou <code>null</code> si pas de lecture possible 
	 */
	public PmsiLineType parseLine() {
		if (linesTypes.get(getState()) == null)
			throw new RuntimeException("Lecture impossible dans l'état actuel");
		for (PmsiLineType lineType : linesTypes.get(getState())) {
			// Récupération du type de ligne (paut être réutilisé)
			Pattern pattern = lineType.getPattern();
			Matcher match = pattern.matcher(toParse);

			// On a une ligne qui correspond
			if (match.matches()) {
				for (int i = 0 ; i < match.groupCount() ; i++) {
					lineType.setContent(i, match.group(i + 1));
				}
				// Suppression du match de la ligne
				toParse = toParse.substring(match.end());

				// Renvoi de l'objet correspondant à la ligne lue
				return lineType;
			}
		}
		
		// Pas de ligne correctement lue, on renvoie null
		return null;
	}
	
	/**
	 * Suppression du contenu du buffer stockant la ligne actuelle
	 */
	public void flushLine() {
		toParse = new String();
	}

	/**
	 * Fonction à appeler à la fin du fichier
	 * @throws Exception 
	 */
	public abstract void endOfFile() throws PmsiReaderException;
	
	/**
	 * Fonction à appeler pour réaliser le travail de cette classe
	 * @return 
	 * @throws PmsiWriterException
	 * @throws PmsiReaderException
	 */
	public abstract void process() throws PmsiWriterException, PmsiReaderException;
	
	/**
	 * Fonction permettant de libérer les ressources créées par cet objet
	 * @throws PmsiWriterException 
	 * @throws Exception
	 */
	public abstract void close() throws PmsiWriterException;

}
