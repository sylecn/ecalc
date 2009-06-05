package ecalc;

/**
 * help screen get current info from a Machine
 */
public interface IFScreenHelper {

	// see the variables in BaseScreen for what you will need.
	String getNumberOnMainPanel();
	boolean isMinusNumber();

	/**
	 * error_signal is used to tell whether there is a error.
	 * the specific screen can blink or show a read light to signal to user.
	 */
	boolean hasError();
	String getErrorMessage();

	boolean opIsPlus();
	boolean opIsSubtract();
	boolean opIsMultiply();
	boolean opIsDivide();
	Keys getOp();

}
