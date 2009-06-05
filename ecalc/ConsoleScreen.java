package ecalc;

/**
 * an simplemetation of Screen
 */
public class ConsoleScreen extends BaseScreen
	implements IFScreen {

	String getDisplayStr() {
		String opstr = "op:";
		if (op == null) {
			opstr += "none";
		} else {
			opstr += Keys.toString(op);
		}
		String num = "num:" + main_panel;
		String err = "err:";
		if (error_signal) {
			err += error_msg;
		} else {
			err += "none";
		}
		
		return opstr + " " + num + " " + err;
	}
			       
	public void updateScreen () {
		//output format "op:+ num:-123.4 err:none";
		//temply
		// System.out.println(getDisplayStr());
	}

	//for unittest purpose only
	String getMainPanelString() {
		return main_panel;
	}
}
