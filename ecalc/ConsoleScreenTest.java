package ecalc;

import org.junit.*;
import static org.junit.Assert.*;

public class ConsoleScreenTest {

	private ConsoleScreen cs;

	@Before public void setUp() {
		cs = new ConsoleScreen();
	}
	

	@Test public void testInitValue() {
		
		assertEquals("0", cs.getMainPanelString());
	}

}
