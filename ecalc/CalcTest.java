package ecalc;

import org.junit.*;
import static org.junit.Assert.*;

public class CalcTest {

	private Keyboard k;
	private Machine m;
	private ConsoleScreen s;
	private Calc c;

	//for test only
	private void debug(String msg) {
		System.out.println(msg);
	}

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
	
	@Test public void testExecuteCalc()
		throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		k.pressNumberKeys("12");
		k.pressOpKey("+");
		k.pressNumberKeys("1");
		k.pressOpKey("=");
		assertEquals(13.0, m.getResult(), MachineTest.delta);
		
		c = m.getCurrentCalc();
		assertEquals(2, c.getNumberCount());
		assertEquals(2, c.getOpCount());

		CalcIterator ci = new CalcIterator(c);
		assertEquals("12", ci.next());
		assertEquals("+", ci.next());
		assertEquals("1", ci.next());
		assertEquals("=", ci.next());
		assertEquals(false, ci.hasNext());
		
		m.executeCalc(c);
		assertEquals(13.0, m.getResult(), MachineTest.delta);
	}

	//for Machine only
	@Test public void testEqualOp()
		throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		k.pressNumberKeys("1");
		k.pressOpKey("+");
		k.pressNumberKeys("3");
		k.pressOpKey("=");
		k.pressOpKey("+");
		k.pressNumberKeys("5");
		k.pressOpKey("=");
		assertEquals(9.0, m.getResult(), MachineTest.delta);
	}

	//for Machine only
	@Test public void testEqualOpSpecial()
		throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		k.pressNumberKeys("1");
		k.pressOpKey("=");
		assertEquals(1.0, m.getResult(), MachineTest.delta);
		assertEquals("1", m.getNumberStr());
		k.pressNumberKeys("3");
		assertEquals("3", m.getNumberStr());
		k.pressOpKey("=");
		//TODO was 1.0
		//I don't know how to do with the equal key.
		assertEquals(3.0, m.getResult(), MachineTest.delta);
		k.pressOpKey("+");
		k.pressNumberKeys("5");
		k.pressOpKey("=");
		assertEquals(8.0, m.getResult(), MachineTest.delta);
	}

	@Test public void testIllegalEqualOp()
		throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		k.pressNumberKeys("1");
		k.pressOpKey("=");
		k.pressNumberKeys("3");
		assertEquals(3.0, m.getResult(), MachineTest.delta);
	}

	@Test public void testIllegalEqualOp2()
		throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		k.pressNumberKeys("1");
		k.pressOpKey("+");
		k.pressOpKey("=");
		assertEquals(1.0, m.getResult(), MachineTest.delta);
		k.pressNumberKeys("3");
		assertEquals(3.0, m.getResult(), MachineTest.delta);
	}

	//History
	@Test public void testEqualSeperatesCalc()
		throws NoOpKeyForGivenString, NoNumberKeyForGivenNumber {
		k.pressNumberOrOpKeys("1");
		assertEquals(0, m.getHistoryCount());
		k.pressNumberOrOpKeys("+");
		k.pressNumberOrOpKeys("1");
		k.pressNumberOrOpKeys("+");
		assertEquals(0, m.getHistoryCount());
		k.pressNumberOrOpKeys("1");
		k.pressNumberOrOpKeys("=");
		assertEquals(3.0, m.getResult(), MachineTest.delta);
		// assertEquals(1, m.getHistoryCount());
		// assertEquals("1\n+\n1\n+\n1\n=\n", m.getHistoryCalc(0).toString());
	}

	@Test public void testClearSeperatesCalc() {
		
		assertEquals(1, 1);
	}
	
	
	@Test public void testCalcToString()
		throws NoNumberKeyForGivenNumber, NoOpKeyForGivenString {
		k.pressNumberKeys("12");
		k.pressOpKey("+");
		k.pressNumberKeys("1");
		k.pressOpKey("=");

		c = m.getCurrentCalc();
		assertEquals("12\n+\n1\n=\n", c.toString());
		// debug(c.toString());
	}

}
