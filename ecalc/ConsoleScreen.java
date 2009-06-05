package ecalc;

/**
 * an simplemetation of Screen
 */
public class ConsoleScreen extends Screen{

	String getDisplayStr() {
		String opstr = "op:";
		if (op == null) {
			opstr += "none";
		} else {
			opstr += Keys.toString(op);
		}
		String num = "num:" + main_panel;
		return opstr + " " + num;
	}
			       
	void updateScreen () {
		//output format "op:+ num:-123.4";
		System.out.println(getDisplayStr());
	}

	String getMainPanelString() {
		return main_panel;
	}
				    
}
