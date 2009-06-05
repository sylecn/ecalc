package ecalc;

/**
 * comments
 */
public class NoOpKeyForGivenString extends Exception {

	public NoOpKeyForGivenString(String opkey) {
		super("No op key for given string " + opkey);
	}
}
