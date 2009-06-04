package ecalc;

import static org.junit.Assert.*;

import org.junit.Test;


public class MachineTest {
	
	@Test 
	public void testAdd() {
		Machine m = new Machine();
		
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		m.keyPress(Keys.KEY1);
		m.keyPress(Keys.KEY_ADD);
		assertEquals(2.0, m.getResult(), 0.0000001);
	}
	
	@Test
	public void testAddMore() {
		
	}
}
