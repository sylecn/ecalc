package ecalc;

import static org.junit.Assert.*;

import org.junit.Test;


public class KeysTest {

	@Test
		public void testDoOp() {
		assertEquals(2.0, Keys.doOp(Keys.KEY_ADD, 1, 1), 0.0000001);
	}

}
