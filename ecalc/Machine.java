package ecalc;

import java.util.regex.*;

public class Machine {

	private ScreenManager screen;
	
	//previous number
	private double number_old = 0.0;
	//curent number, double and String
	private double number = 0.0;
	private String number_str = "";

	//whether dot is inputed, for error detecting only.
	private boolean after_dot = false;
	
	//current operation
	private Keys op = null;

	//record last typed key
	private Keys last_key = null;
	
	public Machine() {
		screen = new ScreenManager();
	}

	//====================
	// support functions
	//====================

	//for test only
	private void debug(String msg) {
		System.out.println(msg);
	}

	//this is the standard machine console
	//this can direct to GUI or text file later.
	private void console(String msg) {
		//temply
		// System.out.println(msg);
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

	private void addDotNow() {
		after_dot = true;
	}


	//for unit test only
	String getNumberStr() {
		return number_str;
	}

	/**
	 * return cal result. if no cal, return current (partial) input number.
	 */
	public double getResult() {
		// debug("number_str=" + number_str + " op=" + Keys.toString(op));
		if (op != null) {
			return number_old;
		}
		if (! number_str.isEmpty()) {
			return Double.parseDouble(number_str);
		}
		
		return 0.0;
	}


	// formalize
	//   1.33333333333333333333 to 1.3333333333  (12 digits)
	//   3e-8 to 0.00000003
	static final Pattern TRAILING_ZEROS = Pattern.compile("0+$");
	static final Pattern BEGINNING_ZEROS = Pattern.compile("^0+");
	
	String formalizeNumberString(String num) {
		String re = num;

		//TODO
		// double d = Double.parseDouble(re);
		// (if (d < 

		
		if (re.indexOf(".") != -1) {
			// remove trailing zeros
			// re = re.replaceAll("0+$", "");
			// for efficiency, use compiled regexp.
			re = TRAILING_ZEROS.matcher(re).replaceAll("");

			// remove trailing dot
			if (re.endsWith(".")) {
				re = re.substring(0, re.length() - 1);
			}
		}
		
		boolean minus = false;
		if (re.charAt(0) == '-') {
			minus = true;
			re = re.substring(1, re.length());
		}

		// remove heading zeros
		re = BEGINNING_ZEROS.matcher(re).replaceAll("");

		if (re.isEmpty()) {
			re = "0";
		} else {
			if (minus) {
				re = "-" + re;
			}
		}
		
		return re;
	}

	String formalizeNumber(double num) {
		String re = Double.toString(num);
		return formalizeNumberString(re);
	}
					   
					   
	// formalize number and pass to screen
	void showCalcResult(double result) {
		// It's the screen's duty to update sign according to a calc
		// result.
		// if (! number_old < 0) {
		// screen.;
		// }
		number_str = formalizeNumber(result);
		screen.updateResult(number_str);
	}					   

	//====================
	// interact with screen
	//====================

	public void addScreen(Screen d) {
		screen.addScreen(d);
	}
					  
	private void toggleMinusSign() {
		updateNumber(toggleMinusSignForNumber(number_str));
		screen.toggleMinusSign();
	}

	//this is for change the display value only.
	private void updateMainPanel(String number) {
		if (number == "") {
			number = "0";
		}
		screen.updateMainPanel(number);
	}

	//this is for real date change.
	//will have impact on how to generate number.
	private void updateNumber(String number) {
		number_str = number;
		screen.updateMainPanel(number_str);
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

		console("Machine clear.");
	}

	//====================
	// Key function for Machine
	//====================

	public void keyPress(Keys key) {
		console("key press: " + Keys.toString(key) + ".");
		
		if (key == Keys.KEY_CLEAR) {
			// debug("catch KEY_CLEAR.\n");
			clear();
			return;
		}
		//any new input clears the error msg.
		screen.clearErrorMsg();
		console("screen manager: clear error msg.");
		if (Keys.isNumber(key)) {
			if (Keys.isOp(last_key)) {
				updateNumber("");
				console("Machine: clear previous num.");
			}
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
					console("error: there is already a dot in number.");
					return;
				}
			}
			updateNumber(number_str + Keys.toString(key));
			console("Machine: update number string.");
		} else {
			if (op == null) {
				number_old = Double.parseDouble(number_str);
				updateOp(key);
				console("Machine: get op. last number saved.");
			} else {
				if (Keys.isOp(last_key)) {
					updateOp(key);
					console("Machine: " + "change op from " + op + " to " + key);
					return;
				}

				number = Double.parseDouble(number_str);
				number_old = Keys.doOp(op, number_old, number);
				console("Machine: show result now.");
				showCalcResult(number_old);
				// debug("real=" + number_old + " "
				//       +"str=" + getNumberStr());
				updateOp(key);
			}
		}
		last_key = key;
	}
}

// Local Variables:
// c-basic-offset: 8
// End:
