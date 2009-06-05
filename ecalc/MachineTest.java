package ecalc;

import static org.junit.Assert.*;

import org.junit.*;


public class MachineTest {
	private Machine m;
	public static final double delta = 0.00000001;

	void gap() {
		System.out.println();
		System.out.println("====================");
	}

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

	@Test public void testMinusItself() {
		m.keyPress(Keys.KEY_MINUS);
		assertEquals("-", m.getNumberStr());
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

	@Test public void testBackspace() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY3);
		m.keyPress(Keys.KEY_BACKSPACE);
		assertEquals("12", m.getNumberStr());
		m.keyPress(Keys.KEY_DOT);
		assertEquals("12.", m.getNumberStr());
		m.keyPress(Keys.KEY_BACKSPACE);
		assertEquals("12", m.getNumberStr());
		m.keyPress(Keys.KEY_CLEAR);
		m.keyPress(Keys.KEY_BACKSPACE);
		assertEquals("", m.getNumberStr());
		// TODO in screen, test the error msg
	}
	

	@Test public void testError1() {
		m.keyPress(Keys.KEY2);
		m.keyPress(Keys.KEY_CLEAR);
		// assertEquals(, );
	}

	@Test public void testCatchNoSuchOpError() throws NoSuchOperationException {
		try {
			double number_old = Keys.doOp(Keys.KEY2, 1.0, 2.0);
			fail("Should report NoSuchOp exception.");
		} catch (NoSuchOperationException expected) {
			; // Expected - intentional
		}

		try {
			double number_old = Keys.doOp(Keys.KEY_ADD, 1.0, 2.0);
			; // Expected - intentional			
		} catch (NoSuchOperationException expected) {
			fail("Should not report NoSuchOp exception.");
		}
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

	@Test public void testDivideSimple() {
		m.keyPress(Keys.KEY8);
		m.keyPress(Keys.KEY_DIVIDE);
		m.keyPress(Keys.KEY4);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(2, m.getResult(), delta);
	}

	@Test public void testDivideFloatNumberDisplay() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_DIVIDE);
		m.keyPress(Keys.KEY3);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(0.33333333333, m.getResult(), delta);
		// assertEquals("0.33333333333", m.getNumberStr());
	}
	
	@Test public void testSmallNumberDisplay() {
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_DIVIDE);
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY_ADD);
		// assertEquals("0.00000003", m.getNumberStr());
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

	@Test public void testNumberDisplayOver12digits() {
		for (int i = 0; i < 15; ++i) {
			m.keyPress(Keys.KEY1);
		}
		assertEquals("111111111111", m.getNumberStr());
	}

	@Test public void testFormalizeZeros() {
		assertEquals("7", m.formalizeNumber(7.0));
		assertEquals("-127.12", m.formalizeNumber(-127.120));
		assertEquals("70", m.formalizeNumber(70));
		assertEquals("1", m.formalizeNumber(1.000));
		assertEquals("0", m.formalizeNumber(0.000));
		assertEquals("2", m.formalizeNumber(002));
		assertEquals("-2.01", m.formalizeNumber(-002.01));
		assertEquals("-2.01", m.formalizeNumber(-002.010));
		assertEquals("0", m.formalizeNumber(0000));
		assertEquals("0", m.formalizeNumber(-0.0));

		//translate sci notation
		assertEquals("0.00000003", m.formalizeNumber(3e-8));
		assertEquals("0.00000003333", m.formalizeNumber(3.3333333e-8));
		assertEquals("-0.0000000125", m.formalizeNumber(-1.25e-8));
		assertEquals("0.0000013", m.formalizeNumber(1.3e-6));

		//12-bit restriction
		assertEquals("0", m.formalizeNumber(3.3333333e-13));
		assertEquals("888888888888", m.formalizeNumber(3.3333333e13));
		assertEquals("888888888888", m.formalizeNumber(1.0e12));
		assertEquals("111111111111", m.formalizeNumber(111111111111.0));
		assertEquals("112345678901", m.formalizeNumber(1.12345678901e11));
		//trunk, don't round. because it's not important.
		assertEquals("112345678901", m.formalizeNumber(1.123456789016e11));
		assertEquals("0.12345678901", m.formalizeNumber(0.12345678901));
		assertEquals("0.12345678901", m.formalizeNumber(0.123456789012));
		assertEquals("-0.123456789", m.formalizeNumber(-0.1234567890));
		//minus is not displayed in main_panel
		assertEquals("0.12345678901", m.formalizeNumber(-0.12345678901));
		assertEquals("0.12345678901", m.formalizeNumber(-0.123456789012));
		assertEquals("0.12345678901", m.formalizeNumber(-0.123456789018));
	}

	@Test public void testDisplayMinusZero() {
		m.keyPress(Keys.KEY_MINUS);
		m.keyPress(Keys.KEY00);
		assertEquals("-00", m.getNumberStr());
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY00);
		m.keyPress(Keys.KEY_MINUS);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(0, m.getResult(), delta);
		assertEquals("0", m.getNumberStr());
	}

	
}
