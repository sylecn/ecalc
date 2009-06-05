package ecalc;

/**
 * manager all screens attached to a Machine
 */
public class ScreenManager {

	//TODO I don't know how to use pointer or ref for storing class.
	//     So I use static inefficient object array. let gc deal with the
	//     problem.
	private Screen[] screens;
	private int count = 0;

	public ScreenManager() {
		screens = new Screen[20];
	}

	void addScreen(Screen d) {
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
