package ecalc;

/**
 * comments
 */
public class NoSuchOperationException extends Exception{

	public NoSuchOperationException(Keys op) {
		super("No operation for key \"" + Keys.toString(op));
	}


}
