package ecalc;

/**
 * A Calc contains all numbers and ops that the user input in a session.
 * A complete session is a series of key strokes between CLEAR and EQUAL key.
 * An incomplete session is a series without a EQUAL key.
 * An empty session contains no keys. Press CLEAR twice or more gets empty
 * session. empty session will not be recorded in History.
 * A Calc can be build on a series of numbers and ops without interactive with
 * a Machine.
 * 
 * A Calc can be executed on a Machine.
 *
 * Auto detect Calc similarity:
 *   If both of them has more than 10 numbers, and diff between them is smaller
 *   than 5 lines (including number and op), then they are marked as similar.
 * Auto detect interactive Calc similarity:
 *   If a Calc's first 3 number (full number, not number Keys) and op is exactly
 *   the same as another Calc, it is marked as similar with that Calc. When this
 *   happens, if the calc goes different with that previous Calc, an error is
 *   signaled. and error msg contains what's last times input. "warn: you input
 *   18.2 was 158.2". and when the calc goes on, a error msg "diff: 140" is
 *   shown everytime a partial result is issued. if diff is 0, hide it.
 *   That means the user may input two number in different order.
 *
 * An auto detected interactive Calc can be marked as not similar by the user.
 * And stops comparing automatically.
 * 
 * A Calc can do a diff on any other Calc(s). No matter they are similar or not.
 * When doing diff, the orginal Calc is old file, and each other Calc is cmped
 * to it, mark difference with + - marks (and red color in GUI), like "diff -u".
 *
 * When user finish a calc and ask for a diff(click on compare button), a list
 * of all similar calcs are given, and the most recently one is marked to be
 * proceed. The user can select more than one Calc to be compared with the
 * current Calc. (use checkbox in GUI.)
 *
 */
public class Calc {

	private static final int MAX_NUMBER_COUNT = 1000;

	private String[] numbers;
	private int numberp;
	private String[] ops;
	private int opp;

	public Calc() {
		numbers = new String[MAX_NUMBER_COUNT + 1];
		ops = new String[MAX_NUMBER_COUNT + 1];
		numberp = -1;
		opp = -1;
	}

	// public Calc(Calc calc) {
	// }

	public boolean isComplete() {
		return true;
	}

	public boolean isSimilarWith(Calc cal) {
		return true;
	}

	void addNumber(String num) {
		numberp++;
		numbers[numberp] = num;
	}

	void addOp(String op) {
		opp++;
		ops[opp] = op;
	}

	void changeOp(String op) {
		ops[opp] = op;
	}

	int getNumberCount() {
		return numberp + 1;
	}

	int getOpCount() {
		return opp + 1;
	}
	
	String getNumberAt(int index) {
		if ((index >=0) && (index <= numberp)) {
			return numbers[index];
		} else {
			throw new IndexOutOfBoundsException("Calc: error: No number at index " + index);
		}
	}

	String getOpAt(int index) {
		if ((index >=0) && (index <= opp)) {
			return ops[index];
		} else {
			throw new IndexOutOfBoundsException("Calc: error: No op at index " + index);
		}
	}

	public String toString() {
		CalcIterator ci = new CalcIterator(this);
		String re = "";
		while (ci.hasNext()) {
			 re += ci.next() + "\n";
		}
		return re;
	}
}
