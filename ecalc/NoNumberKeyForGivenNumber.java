package ecalc;

/**
 * comments
 */
public class NoNumberKeyForGivenNumber extends Exception {

	public NoNumberKeyForGivenNumber(int number) {
		super("No number key for given number " + number);
	}

}
