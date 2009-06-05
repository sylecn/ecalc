package ecalc;

import static org.junit.Assert.*;

import org.junit.*;


public class MachineTest {
	private Machine m;
	public static final double delta = 0.00000001;

	@Before public void setUp() {
		m = new Machine();
	}
	
	@Test public void testAdd() {
		m.keyPress(Keys.KEY1);
		assertEquals(1, m.getResult(), delta);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(1, m.getResult(), delta);
		m.keyPress(Keys.KEY1);
		assertEquals(1, m.getResult(), delta);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(2.0, m.getResult(), delta);
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
		assertEquals(12.0, m.getResult(), delta);
	}

	@Test public void testKey00_1() {
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(0.0, m.getResult(), delta);
	}
	
	@Test public void testKey00_2() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(100.0, m.getResult(), delta);
	}

	@Test public void testKey00_3() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_DOT);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(1.0, m.getResult(), delta);
	}

	@Test public void testNumberInt() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY5);
		assertEquals(125, m.getResult(), delta);
	}

	@Test public void testNumberFloat1() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_DOT);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY5);
		assertEquals(1.25, m.getResult(), delta);
	}

	@Test public void testNumberFloat2() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_DOT);
		assertEquals(1, m.getResult(), delta);
	}

	@Test public void testMinusNumber() {
		m.keyPress(Keys.KEY_MINUS);
		m.keyPress(Keys.KEY1);
		assertEquals(-1, m.getResult(), delta);
	}

	@Test public void testDoubleMinus() {
		m.keyPress(Keys.KEY_MINUS);
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_MINUS);
		assertEquals(1, m.getResult(), delta);
		m.keyPress(Keys.KEY_MINUS);
		assertEquals(-1, m.getResult(), delta);
	}
	

	@Test public void testClearIsNotNumber() {
		assertEquals(false, Keys.isNumber(Keys.KEY_CLEAR));
	}
	
	@Test public void testClear1() {
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY_CLEAR);
		assertEquals(0, m.getResult(), delta);
	}

	@Test public void testClear2() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY_CLEAR);
		assertEquals(0, m.getResult(), delta);
		m.keyPress(Keys.KEY1);
		assertEquals(1, m.getResult(), delta);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY1);
		assertEquals(1, m.getResult(), delta);
	}

	@Test public void testError1() {
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY_CLEAR);
		// assertEquals(, );
	}

	@Test public void testDefaultNumberOnScreen() {
		
		assertEquals(0, m.getResult(), delta);
	}
	
	@Test public void testChangeOp() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_SUBTRACT);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(2, m.getResult(), delta);
		m.keyPress(Keys.KEY_MULTIPLY);
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_MINUS);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(-200, m.getResult(), delta);
	}
	

	/**
	 * with MINUS CLEAR ADD
	 */
	@Test public void testPracticalLegalAdd() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_MINUS);
		m.keyPress(Keys.KEY2);
		assertEquals(-12, m.getResult(), delta);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(-12, m.getResult(), delta);
		m.keyPress(Keys.KEY6);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(-6, m.getResult(), delta);
		m.keyPress(Keys.KEY7);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(1, m.getResult(), delta);
	}

	@Test public void testAttachScreen() {
		ConsoleScreen d = new ConsoleScreen();
		m.addScreen(d);
		m.keyPress(Keys.KEY1);
	}
	
}
