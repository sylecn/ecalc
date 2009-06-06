package ecalc;

/**
 * Screen is a adapter for all physical screens, which is a Displayer.
 * Screen controls Displayer.
 *
 * almost everything is package level privilege for use in Machine Class.
 */
public class BaseScreen
	implements IFScreen {

	/**
	 * main_panel is the biggest display area.
	 * usually looks like 888,888,888,888.
	 */
	String main_panel = "0";
	boolean minus_sign = false;

	/**
	 * error_signal is used to tell whether there is a error.
	 * the specific screen can blink or show a read light to signal to user.
	 */
	boolean error_signal = false;
	String error_msg = "";

	boolean op_plus = false;
	boolean op_subtract = false;
	boolean op_multiply = false;
	boolean op_divide = false;
	Keys op = null;

	void clearOp() {
		op_plus = false;
		op_subtract = false;
		op_multiply = false;
		op_divide = false;
		op = null;
	}
			     
	public void clear() {
		main_panel = "0";
		minus_sign = false;
		
		error_signal = false;
		error_msg = "";

		clearOp();
		updateScreen();
	}

	void BaseScreen() {
		clear();
	}

	public void toggleMinusSign() {
		minus_sign = ! minus_sign;
		updateScreen();
	}

	public void resetMinusSign(boolean minus) {
		minus_sign = minus;
		updateScreen();
	}

	void setErrorMsgNoNotify(String msg) {
		error_msg = msg;
		error_signal = true;
	}
	
	public void setErrorMsg(String msg) {
		setErrorMsgNoNotify(msg);
		updateScreen();
	}

	public void clearErrorMsg() {
		if (error_signal) {
			error_msg = "";
			error_signal = false;
			updateScreen();
		}
	}

	public void updateMainPanel(String num) {
		main_panel = num;
		updateScreen();
	}

	public void updateOp(Keys op) {
		clearOp();
		this.op = op;
		switch (op) {
		case KEY_ADD:
			op_plus = true;
			break;
		case KEY_SUBTRACT:
			op_subtract = true;
			break;
		case KEY_MULTIPLY:
			op_multiply = true;
			break;
		case KEY_DIVIDE:
			op_divide = true;
			break;
		default:
			/* never reach */
			break;
		}
		updateScreen();
	}

	public String getScreenName() {
		return "BaseScreen";
	}
	public void updateScreen() {}
}