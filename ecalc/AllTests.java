package ecalc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(MachineTest.class));
		suite.addTest(new TestSuite(KeysTest.class));
		//$JUnit-END$
		return suite;
	}

}
