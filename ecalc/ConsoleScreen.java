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
		String num = "num:" + getMainPanelString();
		String err = "err:";
		if (error_signal) {
			err += error_msg;
		} else {
			err += "none";
		}
		
		return opstr + " " + num + " " + err;
	}
			       
	void updateScreen () {
		//output format "op:+ num:-123.4 err:none";
		System.out.println(getDisplayStr());
	}

	String getMainPanelString() {
		if (main_panel.isEmpty()) {
			return "0";
		} else {
			return main_panel;
		}
	}
				    
}
