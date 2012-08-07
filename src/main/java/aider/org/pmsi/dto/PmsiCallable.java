package aider.org.pmsi.dto;

import java.util.concurrent.Callable;

/**
 * Classe permettant de réaliser un traitement et de renvoyer un résultat à la classe
 * parente
 * @author delabre
 *
 */
public class PmsiCallable implements Callable<String> {

	/**
	 * Classe implémentant {@link PmsiDto} à lancer
	 */
	private PmsiDto runnable;
		
	/**
	 * Constructeur
	 * @param runnable sera lancée lors de l'exécution de {@link PmsiCallable#runnable}
	 */
	public PmsiCallable(PmsiDto runnable) {
		this.runnable = runnable;
	}

	@Override
	public String call() throws Exception {
		runnable.transfer();
		return "done";
	}
	
}
