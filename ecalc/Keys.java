package ecalc;

public enum Keys {
	KEY0,
		KEY1,
		KEY2,
		KEY3,
		KEY4,
		KEY5,
		KEY6,
		KEY7,
		KEY8,
		KEY9,
		KEY00,
		KEY_DOT,
		KEY_MINUS,
		KEY_ADD,
		KEY_SUBTRACT,
		KEY_MULTIPLY,
		KEY_DIVIDE,
		KEY_CLEAR,
		KEY_BACKSPACE;
	
	public static boolean isNumber(Keys key) {
		switch (key) {
		case KEY0:
		case KEY1:
		case KEY2:
		case KEY3:
		case KEY4:
		case KEY5:
		case KEY6:
		case KEY7:
		case KEY8:
		case KEY9:
		case KEY00:
		case KEY_DOT:
		case KEY_MINUS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isOp(Keys key) {
		if (key == null) {
			return false;
		} else {
			return ! isNumber(key);
		}
	}
					     

	public static String toString(Keys key) {
		if (key == null) {
			return "none";
		}
		switch (key) {
		case KEY0:
			return "0";
		case KEY1:
			return "1";
		case KEY2:
			return "2";
		case KEY3:
			return "3";
		case KEY4:
			return "4";
		case KEY5:
			return "5";
		case KEY6:
			return "6";
		case KEY7:
			return "7";
		case KEY8:
			return "8";
		case KEY9:
			return "9";
		case KEY00:
			return "00";
		case KEY_DOT:
			return ".";
		case KEY_MINUS:
			return "-";
		case KEY_ADD:
			return "+";
		case KEY_SUBTRACT:
			return "-";
		case KEY_MULTIPLY:
			return "*";
		case KEY_DIVIDE:
			return "/";
		case KEY_CLEAR:
			return "C";
		default:
			/* never reach here. */
			return "INVILID_KEY";
		}
	}

	public static double doOp(Keys op, double n1, double n2) throws NoSuchOperationException {
		switch (op) {
		case KEY_ADD:
			return n1 + n2;
		case KEY_SUBTRACT:
			return n1 - n2;
		case KEY_MULTIPLY:
			return n1 * n2;
		case KEY_DIVIDE:
			//It is upper level program's job to catch exceptions.
			return n1 / n2;
		case KEY_CLEAR:
			//fall through
		default:
			throw new NoSuchOperationException(op);
		}
	}
}
