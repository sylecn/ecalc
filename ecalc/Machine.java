package ecalc;

public class Machine {

	public Screen screen;
	
	//previous number
	private double number_old = 0.0;
	//curent number
	private double number = 0.0;
	private String number_str = "";

	//whether dot is inputed, for error detecting only.
	private boolean after_dot = false;
	
	//current operation
	private Keys op = null;

	//record last typed key
	private Keys last_key = null;
	
	public Machine() {
		screen = new Screen();
	}

	private void debug(String msg) {
		System.out.println(msg);
	}

	private String toggleMinusSignForNumber(String number) {
		if (number.isEmpty()) {
			return "-";
		}
		if (number.charAt(0) == '-') {
			return number.substring(1);
		} else {
			return "-" + number;
		}
	}

	private void toggleMinusSign() {
		number_str = toggleMinusSignForNumber(number_str);
		screen.toggleMinusSign();
	}

	private void addDotNow() {
		after_dot = true;
	}

	private void updateNumberString(String number) {
		number_str = number;
		screen.main_panel = number_str;
	}

	private void updateOp(Keys newop) {
		op = newop;
		screen.updateOp(op);
	}
				   
	/**
	 * clear for Key.KEY_CLEAR
	 */	  
	private void clear() {
		number = 0.0;
		number_str = "";
		number_old = 0.0;
		after_dot = false;
		op = null;
		last_key = null;
		
		screen.clear();
	}

	/**
	 * return cal result. if no cal, return current (partial) input number.
	 */
	public double getResult() {
		if (! number_str.isEmpty()) {
			return Double.parseDouble(number_str);
		}
		return number_old;
	}

	public void keyPress(Keys key) {
		if (key == Keys.KEY_CLEAR) {
			// debug("catch KEY_CLEAR.\n");
			clear();
			return;
		}
		//any new input clears the error msg.
		screen.clearErrorMsg();
		if (Keys.isNumber(key)) {
			if (key == Keys.KEY_MINUS) {
				toggleMinusSign();
				return;
			}
			if (key == Keys.KEY_DOT) {
				if (! after_dot) {
					addDotNow();
				} else {
					//TODO notify console
					String msg = ". not accept here. Ignored.";
					screen.setErrorMsg(msg);
					//console.warn("User press dot when there is already a dot.");
					debug(msg);
					return;
				}
			}
			updateNumberString(number_str + Keys.toString(key));
		} else {
			if (op == null) {
				number_old = Double.parseDouble(number_str);
				updateNumberString("");
				updateOp(key);
			} else {
				if ((last_key != null) &&
				    (Keys.isOp(last_key))) {
					// debug("change op from " + op
					//       + " to " + key + ".\n");
					op = key;
					return;
				}
			
				number = Double.parseDouble(number_str);
				number_str = "";
				number_old = Keys.doOp(op, number_old, number);
				op = key;
			}
		}
		last_key = key;
	}
}

// Local Variables:
// unit-test-command: (lambda () (zerop (shell-command "make -s check")))
// End:
