package ecalc;

import org.junit.*;
import static org.junit.Assert.*;

public class CalcTest {

	private Keyboard k;
	private Machine m;
	private ConsoleScreen s;
	private Calc c;

	@Before public void setUp() {
		m = new Machine();
		s = new ConsoleScreen();
		k = new Keyboard();
		k.connectToMachine(m);
		m.addScreen(s);
		c = m.getCurrentCalc();
	}
	
	@Test public void testIsComplete() {
		
		assertEquals(true, c.isComplete());
	}

	@Test public void testSimilar() {
		Calc c2 = new Calc();
		assertEquals(true, c.isSimilarWith(c2));
	}
	

}
