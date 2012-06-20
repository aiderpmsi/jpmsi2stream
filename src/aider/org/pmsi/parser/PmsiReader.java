package aider.org.pmsi.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aider.pmsi2sql.machineState.MachineState;
import org.apache.commons.lang.ObjectUtils.Null;


public abstract class PmsiReader<EnumState, EnumSignal> extends MachineState<EnumState, EnumSignal> {

	/**
	 * Lecteur du fichier permettant de lire des lignes.Attention, la fin de ligne est définie
	 * dans @link {@link BufferedReader#readLine()}
	 */
	private BufferedReader reader;
	
	/**
	 * Définition de l'état de fin de fichier
	 */
	private EnumSignal signalEof;
	
	/**
	 * Stocke la dernière ligne extraite du {@link PmsiReader#pmsiReader) permettant
	 * de la traiter et de la comparer avec les lignes que l'on recherche 
	 */
	private String toParse;
	
	/**
	 * Table de hachage des types de lignes gérées par ce lecteur de fichier PMSI
	 */
	private HashMap<EnumState, List<PmsiLineType>> linesTypes = new HashMap<EnumState, List<PmsiLineType>>();

	protected PmsiReader() { }
	
	/**
	 * Constructeur de la classe permettant de lire un fichier PMSI à partir d'un flux
	 * et d'états définissant la manière de lire un fichier
	 * @param reader Flux à lire.
	 * @param stateReady
	 * @param stateFinished
	 * @param stateEof
	 */
	public PmsiReader(
			Reader reader,
			EnumState stateReady,
			EnumState stateFinished,
			EnumSignal signalEof) {
		// Initialisation de la machine à états
		super(stateReady, stateFinished);

		// Initialisation de la lecture du fichier à importer
		this.reader = new BufferedReader(reader);
		
		// Définition du signal eof
		this.signalEof = signalEof;
	}
	
	/**
	 * Lecture d'une nouvelle ligne : les données de la dernière ligne sont détruites et remplacées
	 * par les données de la ligne suivante
	 * @throws Exception
	 */
	public void readNewLine() {
		try {
			toParse = reader.readLine();
		} catch (IOException e) {
			// Normalement, à ce niveau, pmsiReader doit pouvoir �tre lu
			throw new RuntimeException(e);
		}
		
		if (toParse == null)
			changeState(signalEof);
	}
	
	/**
	 * Retourne la ligne actuelle
	 * @return Ligne actuelle
	 */
	private String getLine() {
		return toParse;
	}
	
	/**
	 * Ajout d'un type de ligne à parser
	 * @param lineId état permettant de 
	 * @param MyLine Définitions de la ligne à lire
	 */
	public void addLineType(EnumState state, PmsiLineType pmsiLine) {
		if (linesTypes.get(state) == null)
			linesTypes.put(state, new ArrayList<PmsiLineType>());
		
		linesTypes.get(state).add(pmsiLine);
	}
	
	/**
	 * Tentative de recherche de correspondance entre la ligne actuellement lue
	 * et les types de ligne que l'on peut rechercher
	 * @return la ligne lue, avec les données récupérées dans le contenu ou {@link Null} si pas de lecture possible 
	 */
	public PmsiLineType parseLine() {
		if (linesTypes.get(getState()) == null)
			throw new RuntimeException("Lecture impossible dans l'état actuel");
		for (PmsiLineType lineType : linesTypes.get(getState())) {
			// Récupération du type de ligne (paut être réutilisé)
			Pattern pattern = lineType.getPattern();
			Matcher match = pattern.matcher(getLine());

			// On a une ligne qui correspond
			if (match.matches()) {
				for (int i = 0 ; i < match.groupCount() ; i++) {
					lineType.setContent(i, match.group(i + 1));
				}
				// Suppression du match de la ligne
				
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
	public void flush_line() {
		toParse = new String();
	}

	/**
	 * Fonction à appeler à la fin du fichier
	 * @throws Exception 
	 */
	abstract public void EndOfFile() throws Exception;
}
