package ecalc;

import static org.junit.Assert.*;

import org.junit.Test;


public class KeysTest {

	@Test
		public void testDoOp() {
		assertEquals(2.0, Keys.doOp(Keys.KEY_ADD, 1, 1), 0.0000001);
	}

	@Test public void testIsOp() {
		assertEquals(true, Keys.isOp(Keys.KEY_ADD));
		assertEquals(true, Keys.isOp(Keys.KEY_SUBTRACT));
		assertEquals(true, Keys.isOp(Keys.KEY_MULTIPLY));
		assertEquals(true, Keys.isOp(Keys.KEY_DIVIDE));
		assertEquals(true, Keys.isOp(Keys.KEY_CLEAR));
		assertEquals(true, Keys.isOp(Keys.KEY_BACKSPACE));
	}
	

}
