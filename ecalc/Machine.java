package ecalc;

import java.text.*;

public class Machine
	implements IFScreenHelper {

	//====================
	// variables
	//====================
	
	private ScreenManager screen;
	private BaseScreen bs;
	
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

	//====================
	// constructor
	//====================
	
	public Machine() {
		screen = new ScreenManager();
		bs = new BaseScreen();
		screen.addScreen(bs);
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

	private void addDotNow() {
		after_dot = true;
	}


	private void addKeyToNumber(Keys key) {
		if (number_str.length() < 12) {
			updateNumber(number_str + Keys.toString(key));
		} else {
			screen.setErrorMsg("digit full");
			console("Machine: warning: digit full. key discarded.");
		}
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
	String formalizeNumberString(String num) {
		String re = num;

		double d = Double.parseDouble(re);
		// debug("formalize: str=" + num + " " + "double=" + d);

		//"digit" overflow
		if (Math.abs(d) < 1e-12) {
			return "0";
		}
		if (Math.abs(d) > 888888888888.0) {
			screen.setErrorMsg("num too big to show.");
			console("Machine: error: num too big to show.");
			return "888888888888";
		}

		NumberFormat formatter = new DecimalFormat("############.############");
		re = formatter.format(d);


		int len = re.length();
		if (len > 12) {
			// debug("raw length > 12");
			String part1 = re.substring(0, 12);
			String part2 = re.substring(12);
			// debug("part1=" + part1 + " part2=" + part2);
			if (part1.indexOf(".") != -1) {
				part1 += part2.charAt(0);
				// debug("found dot. part1=" + part1 + " part2=" + part2);
			}
			if ((part1.charAt(0) == '-') && (part2.length() > 1)) {
				part1 = part1.substring(1) + part2.charAt(1);
				// debug("found minus. part1=" + part1 + " part2=" + part2);
			}
			re = part1;
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
		console("Machine: show result now. result is " + number_str);
	}

	//============================================
	// IFScreenHelper: Helper functions for screen
	//============================================
	public String getNumberOnMainPanel() {
		return bs.main_panel;
	}
	public boolean isMinusNumber() {
		return bs.minus_sign;
	}

	/**
	 * error_signal is used to tell whether there is a error.
	 * the specific screen can blink or show a read light to signal to user.
	 */
	public boolean hasError() {
		return bs.error_signal;
	}
	public String getErrorMessage() {
		return bs.error_msg;
	}

	public boolean opIsPlus() {
		return bs.op_plus;
	}
	public boolean opIsSubtract() {
		return bs.op_subtract;
	}
	public boolean opIsMultiply() {
		return bs.op_multiply;
	}
	public boolean opIsDivide() {
		return bs.op_divide;
	}
	public Keys getOp() {
		return bs.op;
	}

	//====================================
	// Manager screens using ScreenManager
	//====================================

	public void addScreen(IFScreen d) {
		console("Machine: addScreen " + d);
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

	private void backspace() {
		int len = number_str.length();
		if (len == 0) {
			screen.setErrorMsg("backspace key: nothing to delete.");
			console("Machine: backspace key presses, but nothing to delete.");
		} else {
			updateNumber(number_str.substring(0, len - 1));
		}
	}

	//=========================
	// Key function for Machine
	//=========================

	private void processNumberKey(Keys key) {
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
				screen.setErrorMsg(". not accept here. Ignored.");
				console("Machine: warning: num already has a dot in it. this dot is discarded.");
				return;
			}
		}
		addKeyToNumber(key);
		console("Machine: update number string.");
	}

	private void processOpKey(Keys key) {
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
			try {
				number_old = Keys.doOp(op, number_old, number);
			} catch (NoSuchOperationException e) {
				console("Machine: fatal error: NoSuchOp " + e.getMessage());
				return;
			}
			showCalcResult(number_old);
			// debug("real=" + number_old + " "
			//       +"str=" + getNumberStr());
			updateOp(key);
		}
	}

	public void keyPress(Keys key) {
		console("key press: " + Keys.toString(key) + ".");
		
		//====================================
		// special op key: clear and backspace
		//====================================
		if (key == Keys.KEY_CLEAR) {
			// debug("catch KEY_CLEAR.\n");
			clear();
			return;
		}

		//any new input clears the error msg.
		//clear will erase everything, so I leave it above.
		screen.clearErrorMsg();
		console("screen manager: clear error msg.");
		
		if (key == Keys.KEY_BACKSPACE) {
			backspace();
			return;
		}
		
		if (Keys.isNumber(key)) {
			processNumberKey(key);
		} else {
			processOpKey(key);
		}
		last_key = key;
	}
}

// Local Variables:
// c-basic-offset: 8
// End:
