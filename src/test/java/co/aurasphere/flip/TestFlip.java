package co.aurasphere.flip;

import org.junit.Assert;
import org.junit.Test;

public class TestFlip {

	@Test
	public void testInstantiationWithoutConstructor() throws FlipException {
		TestConstructionObject testObj = Flip.instantiateWithoutConstructor(TestConstructionObject.class);
		Assert.assertNotNull(testObj);
		Assert.assertNull(testObj.getTestString());
	}
	
	public class TestConstructionObject {

		private String testString;

		public TestConstructionObject() {
			this.testString = "HEY THIS TEST HAS NOT PASSED!";
		}

		public String getTestString() {
			return testString;
		}

	}


}
