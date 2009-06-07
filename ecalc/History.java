package ecalc;

/**
 * History is part of a Machine.
 * It stores MAX_CALC_IN_HISTORY most recent Calcs.
 */
public class History {

	private static final int MAX_CALC_IN_HISTORY = 20;
	private Calc[] calcs;

	public History () {
		calcs = new Calc[MAX_CALC_IN_HISTORY];
	}

	public void addCalc(Calc c) {
	}
}
