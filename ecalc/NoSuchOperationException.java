package ecalc;

/**
 * comments
 */
public class NoSuchOperationException extends Exception{

	public final String attrName;

	public NoSuchOperationException(Keys op) {
		super("No operation for key \"" + op + "\" found");
		attrName = Keys.toString(op);
	}


}
