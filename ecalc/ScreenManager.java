package ecalc;

/**
 * manager all screens attached to a Machine
 */
public class ScreenManager {

	//INFO I don't know how to use pointer or ref for storing class.
	//     So I use static inefficient object array. let gc deal with the
	//     problem.
	private IFScreen[] screens;
	private int count = 0;

	public ScreenManager() {
		screens = new IFScreen[20];
	}

	void addScreen(IFScreen d) {
		screens[count] = d;
		count++;
	}

	void toggleMinusSign() {
		for (int i = 0; i < count; ++i) {
			screens[i].toggleMinusSign();
		}
	}

	void updateMainPanel(String num) {
		for (int i = 0; i < count; ++i) {
			screens[i].updateMainPanel(num);
		}
	}

	void updateResult(String re) {
		for (int i = 0; i < count; ++i) {
			screens[i].updateMainPanel(re);
			if (re.charAt(0) == '-') {
				screens[i].resetMinusSign(true);
			} else {
				screens[i].resetMinusSign(false);
			}
		}
	}				     

	void updateOp(Keys op) {
		for (int i = 0; i < count; ++i) {
			screens[i].updateOp(op);
		}

	}
				      
	void clear() {
		for (int i = 0; i < count; ++i) {
			screens[i].clear();
		}

	}

	void clearErrorMsg() {
		for (int i = 0; i < count; ++i) {
			screens[i].clearErrorMsg();
		}

	}

	void setErrorMsg(String msg) {
		for (int i = 0; i < count; ++i) {
			screens[i].setErrorMsg(msg);
		}

	}

	
}
