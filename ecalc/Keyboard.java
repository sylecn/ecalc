package ecalc;

import java.util.regex.*;

/**
 * general keyboard class, provide easy keyboard input to Machine.
 */
public class Keyboard {

	private Machine m;

	private void debug(String msg) {
		System.out.println(msg);
	}
	
	public void connectToMachine(Machine m) {
		if (m != null) {
			this.m = m;
		}
		//TODO signal error: trying to connect to nothing.
	}

	static final Pattern PATTERN_NUMBER = Pattern.compile("-?[0-9]+.?[0-9]*");
	private static boolean isNumber(String str) {
		return PATTERN_NUMBER.matcher(str).matches();
	}

	public void pressKey(Keys key) {
		if (m != null) {
			m.keyPress(key);
		}
		//TODO signal error: not connected.
	}

	public void pressNumberKey(int key) throws NoNumberKeyForGivenNumber {
		switch (key) {
		case 1:
			pressKey(Keys.KEY1);
			break;
		case 2:
			pressKey(Keys.KEY2);
			break;
		case 3:
			pressKey(Keys.KEY3);
			break;
		case 4:
			pressKey(Keys.KEY4);
			break;
		case 5:
			pressKey(Keys.KEY5);
			break;
		case 6:
			pressKey(Keys.KEY6);
			break;
		case 7:
			pressKey(Keys.KEY7);
			break;
		case 8:
			pressKey(Keys.KEY8);
			break;
		case 9:
			pressKey(Keys.KEY9);
			break;
		case 0:
			pressKey(Keys.KEY0);
			break;
		case 100:
			pressKey(Keys.KEY00);
			break;
		default:
			throw new NoNumberKeyForGivenNumber(key);
		}
	}

	public void clear() {
		pressKey(Keys.KEY_CLEAR);
	}

	public void pressNumberKeys(String keyseries) throws NoNumberKeyForGivenNumber {
		char key;
		for (int i = 0; i < keyseries.length(); ++i) {
			key = keyseries.charAt(i);
			switch (key) {
			case '-':
				pressKey(Keys.KEY_MINUS);
				break;
			case '.':
				pressKey(Keys.KEY_DOT);
				break;
			default:
				pressNumberKey(key - '0');
			}
		}
	}

	public void pressOpKey(String opkey) throws NoOpKeyForGivenString {
		if (opkey.equals("+")) {
			pressKey(Keys.KEY_ADD);
			return;
		}
		if (opkey.equals("-")) {
			pressKey(Keys.KEY_SUBTRACT);
			return;
		}
		if (opkey.equals("*")) {
			pressKey(Keys.KEY_MULTIPLY);
			return;
		}
		if (opkey.equals("/")) {
			pressKey(Keys.KEY_DIVIDE);
			return;
		}
		if (opkey.equals("c") || opkey.equals("clear")) {
			pressKey(Keys.KEY_CLEAR);
			return;
		}
		if (opkey.equals("b") || opkey.equals("backspace")) {
			pressKey(Keys.KEY_BACKSPACE);
			return;
		}
		if (opkey.equals("=")) {
			pressKey(Keys.KEY_EQUAL);
			return;
		}
		//never reacher here
		throw new NoOpKeyForGivenString(opkey);
	}

	public void pressNumberOrOpKeys(String key) throws NoOpKeyForGivenString, NoNumberKeyForGivenNumber {
		if (isNumber(key)) {
			// debug("You input a number.");
			pressNumberKeys(key);
		} else {
			// debug("You input an op.");
			pressOpKey(key);
		}
	}
}
