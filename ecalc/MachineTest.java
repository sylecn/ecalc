package ecalc;

import static org.junit.Assert.*;

import org.junit.*;


public class MachineTest {
	private Machine m;

	@Before public void setUp() {
		m = new Machine();
	}
	
	@Test public void testAdd() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(2.0, m.getResult(), 0.0000001);
	}
	
	@Test public void testAddMore() {
		m.keyPress(Keys.KEY5);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY_SUBTRACT);
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_MULTIPLY);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(12.0, m.getResult(), 0.0000001);
	}
}
