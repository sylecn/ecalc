package ecalc;

import java.util.Iterator;

/**
 * calc iterator
 */
public class CalcIterator implements Iterator<String> {

	private Calc c;
	
	private int numberp;
	private int opp;
	private boolean read_number;

	private int number_count;
	private int op_count;

	//for test only
	private void debug(String msg) {
		System.out.println(msg);
	}

	public CalcIterator(Calc c) {
		this.c = c;
		number_count = c.getNumberCount();
		op_count = c.getOpCount();
		numberp = -1;
		opp = -1;
		read_number = true;
	}

	public void remove() {
	}

	public String next() {
		if (read_number) {
			numberp++;
			read_number = false;
			return c.getNumberAt(numberp);
		} else {
			opp++;
			read_number = true;
			return c.getOpAt(opp);
		}
	}

	public boolean hasNext() {
		if (read_number) {
			debug("hasNext: numberp=" + numberp
			      + " number_count=" + number_count);
			if ((number_count > 0) && (numberp < number_count)) {
				return true;
			} else {
				return false;
			}
		} else {
			if ((op_count > 0) && (opp < op_count)) {
				return true;
			} else {
				return false;
			}
		}
	}

}
