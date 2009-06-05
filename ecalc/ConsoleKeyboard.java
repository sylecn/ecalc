package ecalc;

import java.io.*;
import java.text.*;
import java.util.regex.*;

/**
 * a simple keyboard.
 * connects to Machine and ConsoleScreen. makes a standalone calc.
 */
public class ConsoleKeyboard extends Keyboard {

	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	static final Pattern PATTERN_NUMBER = Pattern.compile("-?[0-9]+.?[0-9]*");
	private static boolean isNumber(String str) {
		return PATTERN_NUMBER.matcher(str).matches();
	}

	private static void info(String msg) {
		System.out.println(msg);
	}

	private static void debug(String msg) {
		System.out.println(msg);
	}

	public static void main(String[] args) {
		Machine m = new Machine();
		ConsoleScreen cs = new ConsoleScreen();
		ConsoleKeyboard k = new ConsoleKeyboard();
		k.connectToMachine(m);
		m.addScreen(cs);

		info("Welcome to ConsoleKeyboard!");
		info("Sample Usage, press RET after each line:");
		info("123");
		info("+");
		info("456");
		info("+");
		info("exit");
		info("----");

		String line = "";
		while (true) {
			try {
				line = in.readLine();
				if (line.equals("exit")) {
					break;
				}
				if (isNumber(line)) {
					// debug("You input a number.");
					k.pressNumberKeys(line);
				} else {
					// debug("You input an op.");
					k.pressOpKey(line);
				}
			} catch (IOException e) {
				info(e.getMessage());
			} catch (NoNumberKeyForGivenNumber e) {
				info(e.getMessage());
			} catch (NoOpKeyForGivenString e) {
				info(e.getMessage());
			}
		}

		info("bye.");
	}
	
}
