package ecalc;

/**
 * screen is a base class for all physical screens.
 * it is part of a Machine.
 *
 * almost everything is package level privilege for use in Machine Class.
 */
public class Screen {

	//TODO every var change should trigger an event to tell screen.

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

	void clearOp() {
		op_plus = false;
		op_subtract = false;
		op_multiply = false;
		op_divide = false;
	}
			     
	void clear() {
		main_panel = "0";
		minus_sign = false;
		
		error_signal = false;
		error_msg = "";

		clearOp();
	}

	void Screen() {
		clear();
	}

	void toggleMinusSign() {
		minus_sign = ! minus_sign;
	}

	void setErrorMsg(String msg) {
		error_msg = msg;
		error_signal = true;
	}

	void clearErrorMsg() {
		error_msg = "";
		error_signal = false;
	}

	void updateOp(Keys op) {
		clearOp();
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
	}
						     
}
