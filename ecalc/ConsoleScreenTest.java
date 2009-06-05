package ecalc;

import org.junit.*;
import static org.junit.Assert.*;

public class ConsoleScreenTest {

	private ConsoleScreen cs;
	private Machine m;

	@Before public void setUp() {
		cs = new ConsoleScreen();
		m = new Machine();
		m.addScreen(cs);
	}
	

	@Test public void testInitValue() {
		
		assertEquals("0", cs.getMainPanelString());
	}

	@Test public void testWhenNotAttached() {
		Machine n = new Machine();
		n.keyPress(Keys.KEY1);
		n.keyPress(Keys.KEY2);
		assertEquals("op:none num:0 err:none", cs.getDisplayStr());
	}
	

	@Test public void testNumberDisplay() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY3);
		m.keyPress(Keys.KEY_DOT);
		m.keyPress(Keys.KEY5);
		assertEquals("op:none num:123.5 err:none", cs.getDisplayStr());
	}
	
	@Test public void testNumberDisplayOver12digits() {
		for (int i = 0; i < 15; ++i) {
			m.keyPress(Keys.KEY1);
		}
		assertEquals("op:none num:111111111111 err:digit full", cs.getDisplayStr());
	}

	@Test public void testMinusNumberDisplay() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_DOT);
		m.keyPress(Keys.KEY5);
		m.keyPress(Keys.KEY_MINUS);
		m.keyPress(Keys.KEY3);
		assertEquals("op:none num:-1.53 err:none", cs.getDisplayStr());
	}

	@Test public void testOpShoudNotClearPreviousNumber() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		assertEquals("op:+ num:1 err:none", cs.getDisplayStr());
	}

	@Test public void testOpShoudNotClearPreviousNumber2() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY2);
		assertEquals("op:+ num:2 err:none", cs.getDisplayStr());
	}

	
	//op should not clear previous number if user haven't press a number key
	//That's the way why there is a big ADD, but no EQUAL on my Keyboard
	@Test public void testOp() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY3);
		assertEquals("op:+ num:3 err:none", cs.getDisplayStr());
		m.keyPress(Keys.KEY_ADD);
		assertEquals(4, m.getResult(), MachineTest.delta);
		assertEquals("op:+ num:4 err:none", cs.getDisplayStr());
	}
	
}
