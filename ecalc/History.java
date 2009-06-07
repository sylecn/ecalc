package ecalc;

/**
 * History is part of a Machine.
 * It stores MAX_CALC_IN_HISTORY most recent Calcs.
 */
public class History {

	private static final int MAX_CALC_IN_HISTORY = 20;
	private Calc[] c;
	private int cp;

	public History () {
		c = new Calc[MAX_CALC_IN_HISTORY];
		cp = -1;
	}

	public void addCalc(Calc c) {
	}

	public Calc getCalcAt(int index) {
		if ((index >=0) && (index <= cp)) {
			return c[cp];
		} else {
			throw new IndexOutOfBoundsException("History: error: No Calc at index " + index);
		}
	}
}
