package aider.org.machinestate;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * <p>
 * Machine à états avec transitions par signaux.
 * Template pouvant être utilisée avec toutes sortes de signaux et états
 * à partir du moment où ils sont comparables
 * @author delabre
 *
 * @param <EnumState> classe d'énumération des états
 * @param <EnumSignal> classe d'énumération des signaux
 */
public abstract class MachineState<EnumState, EnumSignal, ReturnType> implements Callable<ReturnType>{

	/**
	 * Définition de l'état où la machine doit d'arrêter
	 */
	private EnumState stateFinished;
	
	
	/**
	 * Indicateur définissant l'état actuel
	 */
	private EnumState stateActual;
	
	/**
	 * La table de changement d'état associe un signal
	 * à un chamgement d'état à partir d'un état initial :
	 */
	HashMap<EnumSignal, HashMap<EnumState, EnumState>> transitionsTable =
			new HashMap<EnumSignal, HashMap<EnumState,EnumState>>();
	
	/**
	 * Interdiction de construire une machine à états sans définir l'état initial
	 * et l'état final
	 */
	protected MachineState() {}
	
	/**
	 * Création de la machine à états avec la définition de l'état initial et
	 * l'état final
	 * @param stateReady
	 * @param stateFinished
	 */
	public MachineState(EnumState stateReady, EnumState stateFinished) {
		this.stateActual = stateReady;
		this.stateFinished = stateFinished;
	}
	
	/**
	 * Insertion d'une transition entre signal, état initial et état final. Le caractère protégé de cette méthode
	 * implique qu'elle ne doit être utilisé que pour surcharger cette classe. Les définitions états ne doivent
	 * jamais être modifés pendant la marche de la machine. Si on en a besoin, c'est que le design des
	 * signaux et états n'est pas complet
	 * @param signal
	 * @param stateInitial
	 * @param stateFinal
	 */
	protected void addTransition(EnumSignal signal, EnumState stateInitial, EnumState stateFinal) {
		// Ajout du signal dans la table des transitions s'il n'existe pas
		if (!transitionsTable.containsKey(signal)) {
			transitionsTable.put(signal, new HashMap<EnumState, EnumState>());
		}
		
		// Lien entre ce signal et cette transition de l'état stateInitial à l'état stateFinal 
		transitionsTable.get(signal).put(stateInitial, stateFinal);
	}

	/**
	 * Modification de l'état de la machine selon le signal donné. Le caractère protégé de cette méthode
	 * implique qu'elle ne doit être utilisé que pour surcharger cette classe. Les définitions états ne doivent
	 * jamais être modifés pendant la marche de la machine. Si on en a besoin, c'est que le design des
	 * signaux et états n'est pas correct
	 * @param signal
	 * @throws MachineStateException 
	 */
	protected void changeState(EnumSignal signal) throws MachineStateException {
		EnumState newState = transitionsTable.get(signal).get(stateActual);
		if (newState == null)
			throw new MachineStateException ("Signal " + signal.toString() + " indéterminé dans l'état actuel " + stateActual.toString() + " de la machine à états");
		else
			stateActual = newState; 
	}
	
	/**
	 * Retourne l'état de la machine. Le caractère protégé de cette méthode
	 * implique qu'elle ne doit être utilisé que pour surcharger cette classe.
	 * @return Etat de la machine
	 */
	protected EnumState getState() {
		return stateActual;
	}
	
	/**
	 * Lancement de la machine à états
	 * @throws Exception
	 */
	public final ReturnType call() throws Exception {
		begin();
		
		while (stateActual != stateFinished) {
			process();
		}
		
		finish();
		
		return getEndMessage();
	}
	
	/**
	 * Méthode à implémenter pour réaliser une action au début du fonctionnement de
	 * la machine à états
	 * @throws Exception
	 */
	protected void begin() throws Exception {};
	
	/**
	 * Méthode à implémenter pour lancer la machine à états
	 * @throws Exception
	 */
	protected void process() throws Exception {};
	
	/**
	 * Méthode à implémenter pour réaliser une action à la fin du fonctionnement de
	 * la machine à états
	 * @throws Exception
	 */
	protected void finish() throws Exception {};
	
	/**
	 * Méthode à implémenter pour renvoyer un message lors de la fin du fonctionnement de
	 * la machine à états
	 * @throws Exception
	 */
	protected ReturnType getEndMessage() {
		return null;
	}
	
}
