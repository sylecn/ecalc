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

	public static void main(String[] args) {
		Machine m = new Machine();
		ConsoleScreen cs = new ConsoleScreen();
		ConsoleKeyboard k = new ConsoleKeyboard();
		k.connectToMachine(m);

		String line = "";
		while (! line.equals("exit")) {
			try {
				line = in.readLine();
				if (isNumber(line)) {
					k.pressNumberKeys(line);
				} else {
					k.pressOpKey(line);
				}
			} catch( Exception e ) {
				System.out.println("type exit to exit.");
			}
		}
	}
	
}
