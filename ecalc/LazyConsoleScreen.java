package ecalc;

/**
 * an lazy update screen
 */
public class LazyConsoleScreen extends ConsoleScreen{

	private String laststr = "";

	private static void debug(String msg) {
		System.out.println(msg);
	}

	void updateScreen () {
		//output format "op:+ num:-123.4 err:none";
		String thisstr = getDisplayStr();
		// debug("thisstr=" + thisstr + " laststr=" + laststr);
		if (! thisstr.equals(laststr)) {
			System.out.println(thisstr);
		}
		laststr = thisstr;
	}
}
