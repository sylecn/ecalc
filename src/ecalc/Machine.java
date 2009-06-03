package ecalc;

public class Machine {
	
	//previous number
	private double number_old = 0.0;
	//curent number
	private double number = 0.0;
	private String number_str = "";

	//whether dot is inputed, for error detecting only.
	private boolean after_dot = false;
	
	//current operation
	private Keys op = null;
	
	public Machine () {
		
	}

	public double getResult() {
		return number_old;
	}

	public void keyPress(Keys key) {
		if (key.isNumber(key)) {
			if (key == Keys.KEY_DOT) {
				if (! after_dot) {
					after_dot = true;
				} else {
					//TODO notify screen and console
					//screen.warn(". not accept here.");
					//console.warn("User press dot when there is already a dot.");
					return;
				}
			}
			number_str += key.toString(key);
		} else {
			if (op == null) {
				number_old = Integer.parseInt(number_str);
				number_str = "";
				op = key;
			} else {
				number = Integer.parseInt(number_str);
				number_str = "";
				number_old = Keys.doOp(op, number_old, number);
				op = key;
			}
		}
	}
}

