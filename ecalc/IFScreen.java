package ecalc;

/**
 * comments
 */
interface IFScreen {

	void toggleMinusSign();
	void resetMinusSign(boolean minus);
	void updateOp(Keys op);
	void updateMainPanel(String num);
	
	void clear();
	
	void clearErrorMsg();
	void setErrorMsg(String msg);

	void updateScreen();
}
