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
	}

	@Test public void testIsComplete() {
		//TODO
		c = m.getCurrentCalc();
		assertEquals(true, c.isComplete());
	}

	@Test public void testSimilar() {
		Calc c2 = new Calc();
		//TODO
		c = m.getCurrentCalc();
		assertEquals(true, c.isSimilarWith(c2));
	}
	
	@Test public void testExecuteEmptyCalc() throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		c = new Calc();
		m.executeCalc(c);
		assertEquals(0, m.getResult(), MachineTest.delta);
	}
	
	@Test public void testExecuteCalc() throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		// m.turnOnConsole();
		k.pressNumberKeys("12");
		k.pressOpKey("+");
		k.pressNumberKeys("1");
		k.pressOpKey("=");
		assertEquals(13.0, m.getResult(), MachineTest.delta);
		
		c = m.getCurrentCalc();
		assertEquals(2, c.getNumberCount());
		assertEquals(2, c.getOpCount());

		CalcIterator ci = new CalcIterator(c);
		assertEquals(true, ci.hasNext());
		assertEquals("12", ci.next());
		assertEquals("+", ci.next());
		assertEquals("1", ci.next());
		assertEquals("=", ci.next());
		m.executeCalc(c);
		assertEquals(13.0, m.getResult(), MachineTest.delta);
	}
}
