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

	@Test public void testNumberDisplay() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY3);
		m.keyPress(Keys.KEY_DOT);
		m.keyPress(Keys.KEY5);
		assertEquals("op:none num:123.5", cs.getDisplayStr());
	}
	
}
