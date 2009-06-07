package ecalc;

import org.junit.*;
import static org.junit.Assert.*;

public class CalcIteratorTest {

	private Calc c;
	private CalcIterator ci;

	@Before public void setUp() {
		c = new Calc();
	}
	
	@Test public void testEmptyCalc() {
		ci = new CalcIterator(c);
		assertEquals(false, ci.hasNext());
	}

}
