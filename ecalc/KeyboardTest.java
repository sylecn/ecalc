package ecalc;

import org.junit.*;
import static org.junit.Assert.*;

public class KeyboardTest {

	private Keyboard k;
	private Machine m;

	@Before public void setUp() {
		k = new Keyboard();
		m = new Machine();
		k.connectToMachine(m);
	}
	
	@Test public void testPressNumberKey() throws NoNumberKeyForGivenNumber {
		k.pressNumberKey(1);
		assertEquals("1", m.getNumberStr());
		k.pressNumberKey(2);
		k.pressNumberKey(3);
		k.pressNumberKey(1);
		assertEquals("1231", m.getNumberStr());
	}

	@Test public void testPressNumberKeys() throws NoNumberKeyForGivenNumber {
		k.pressNumberKeys("-123.40");
		assertEquals("-123.40", m.getNumberStr());
		k.clear();
		k.pressNumberKeys("12-");
		assertEquals("-12", m.getNumberStr());
		k.pressNumberKeys("3.40");
		assertEquals("-123.40", m.getNumberStr());
	}
}
