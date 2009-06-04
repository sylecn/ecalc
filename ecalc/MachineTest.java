package ecalc;

import static org.junit.Assert.*;

import org.junit.*;


public class MachineTest {
	private Machine m;
	private double delta = 0.00000001;

	@Before public void setUp() {
		m = new Machine();
	}
	
	@Test public void testAdd() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY1);
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

	@Test public void testNumberFloat() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_DOT);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY5);
		assertEquals(1.25, m.getResult(), delta);
		m.keyPress(Keys.KEY_CLEAR);
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
	
}
