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
	 * La table de changement d'état associe un signal
	 * à un chamgement d'état à partir d'un état initial :
	 */
	HashMap<EnumSignal, HashMap<EnumState, EnumState>> transitionsTable =
			new HashMap<EnumSignal, HashMap<EnumState,EnumState>>();
	
	/**
	 * Indicateur définissant l'état actuel
	 */
	private EnumState stateActual;
	
	/**
	 * Interdiction de construire une machine à états sans définir l'état initial
	 * et létat final
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
	 * Insertion d'une transition entre signal, état initial et état final
	 * @param signal
	 * @param stateInitial
	 * @param stateFinal
	 */
	public void addTransition(EnumSignal signal, EnumState stateInitial, EnumState stateFinal) {
		if (!transitionsTable.containsKey(signal)) {
			transitionsTable.put(signal, new HashMap<EnumState, EnumState>());
		}
		transitionsTable.get(signal).put(stateInitial, stateFinal);
	}

	/**
	 * Modification de l'état de la machine selon le signal donné
	 * @param signal
	 * @return Le nouvel état de la machine à états
	 */
	public EnumState changeState(EnumSignal signal) {
		EnumState newState = transitionsTable.get(signal).get(stateActual);
		if (newState == null)
			throw new RuntimeException ("Signal " + signal.toString() + " indéterminé dans l'état actuel " + stateActual.toString() + " de la machine à états");
		else
			stateActual = newState; 
		return stateActual;
	}
	
	/**
	 * Retourne l'état de la machine
	 * @return Etat de la machine
	 */
	public EnumState getState() {
		return stateActual;
	}
	
	/**
	 * Lancement de la machine à états
	 * @throws Exception
	 */
	public ReturnType call() throws Exception {
		while (stateActual != stateFinished) {
			process();
		}
		return finish();
	}
	
	/**
	 * Méthode à implémenter pour lancer la machine à états
	 * @throws Exception
	 */
	public abstract void process() throws Exception;
	
	/**
	 * Méthode à implémenter pour réaliser une action à la fin du fonctionnement de
	 * la machine à états
	 * @return Un objet de type ReturnType renvoyé par l'appel à Call
	 * @throws Exception
	 */
	public abstract ReturnType finish() throws Exception;
}
