/*
 * MIT License
 * 
 * Copyright (c) 2017 Donato Rimenti
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package co.aurasphere.flip;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import co.aurasphere.flip.model.SupportObject;
import co.aurasphere.flip.model.WrappedObjectHeavyImpl;
import co.aurasphere.flip.model.WrappedObjectLightImpl;

/**
 * Test class for {@link Flip}.
 * 
 * @author Donato Rimenti
 *
 */
public class TestFlip {

	/**
	 * Tests {@link Flip#getUnsafe()} by creating an object without invoking the
	 * constructor.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInstantiationWithoutConstructor() throws Exception {
		// Creates a new object.
		SupportObject testObj = (SupportObject) Flip.getUnsafe()
				.allocateInstance(SupportObject.class);

		// Checks that the object has been instantiated and that the constructor
		// has not been invoked by checking the value of a field that the
		// constructor would have initialized.
		Assert.assertNotNull(testObj);
		Assert.assertNull(testObj.getTestString());
	}

	/**
	 * Tests {@link Flip#getClassAddress(Object)} and
	 * {@link Flip#getObjectAddress(Object)}.
	 */
	@Test
	public void testAddresses() {
		String testOne = "Test 1";
		String testTwo = "Test 2";
		String testThree = "Test 2";
		SupportObject testFour = new SupportObject();
		SupportObject testFive = new SupportObject();

		// Checks identity.
		Assert.assertEquals(Flip.getClassAddress(testOne),
				Flip.getClassAddress(testOne));

		// Checks same classes.
		Assert.assertEquals(Flip.getClassAddress(testTwo),
				Flip.getClassAddress(testOne));
		Assert.assertEquals(Flip.getClassAddress(testFour),
				Flip.getClassAddress(testFive));

		// Checks different classes.
		Assert.assertNotEquals(Flip.getClassAddress(testFour),
				Flip.getClassAddress(testThree));

		// Checks objects and classes not same address.
		Assert.assertNotEquals(Flip.getClassAddress(testFour),
				Flip.getObjectAddress(testFour));
		Assert.assertNotEquals(Flip.getClassAddress(testOne),
				Flip.getObjectAddress(testOne));

		// Checks objects addresses not the same.
		Assert.assertNotEquals(Flip.getObjectAddress(testOne),
				Flip.getObjectAddress(testTwo));
		Assert.assertNotEquals(Flip.getObjectAddress(testFour),
				Flip.getObjectAddress(testFive));

		// Checks addresses of classes objects are the same (polymorphism is
		// irrelevant).
		Assert.assertEquals(Flip.getObjectAddress(String.class),
				(Object) Flip.getObjectAddress(String.class));

		// String interning.
		Assert.assertEquals(Flip.getObjectAddress(testTwo),
				Flip.getObjectAddress(testThree));
	}

	/**
	 * Tests {@link Flip#readObject(Object)}.
	 */
	@Test
	public void testReadObject() {
		// Test objects.
		SupportObject objOne = new SupportObject("Object 1");
		SupportObject cloneObjOne = Flip.readObject(Flip
				.getObjectAddress(objOne));

		// We are reading the same object so the reference is the same.
		Assert.assertEquals(cloneObjOne, objOne);
	}

	/**
	 * Tests {@link Flip#dynamicClassDefinition(String, String, String)}.
	 */
	@Test
	public void testDynamicClassDefinition() throws Exception {
		// Builds a mock class.
		String className = "TestDynamicClass";
		String classPackage = "co.aurasphere.flip";
		String methodName = "getOutcome";
		StringBuilder classSource = new StringBuilder();
		classSource.append("package ").append(classPackage).append(";");
		classSource.append("public class ").append(className).append(" {\n");
		classSource.append(" public boolean ").append(methodName)
				.append("() {\n");
		classSource.append("     return true;");
		classSource.append(" }\n");
		classSource.append("}\n");

		// Compile it.
		Class<?> newClass = Flip.dynamicClassDefinition(classPackage,
				className, classSource.toString());

		// Calls the method and checks the result.
		boolean outcome = (Boolean) newClass.getMethod(methodName).invoke(
				newClass.newInstance());
		Assert.assertTrue(outcome);
	}

	/**
	 * Tests {@link Flip#shallowCopy(Object)} and
	 * {@link Flip#shallowCopy(Object, Object)}.
	 */
	@Test
	public void testShallowCopy() {
		// Test objects.
		SupportObject objOne = new SupportObject("Object 1");
		SupportObject objTwo = new SupportObject("Object 2");

		// This is just a reference.
		SupportObject objThree = objTwo;

		// Checks preconditions.
		Assume.assumeFalse(objOne.equals(objTwo));
		Assume.assumeTrue(objTwo.equals(objThree));
		Assume.assumeFalse(objOne.getTestString()
				.equals(objTwo.getTestString()));
		Assume.assumeTrue(objTwo.getTestString().equals(
				objThree.getTestString()));

		// Copy object one to a new memory location and check that the new
		// object has the same value but different reference to it.
		SupportObject objFour = Flip.shallowCopy(objOne);
		Assert.assertEquals(objOne.getTestString(), objFour.getTestString());
		Assert.assertNotEquals(objOne, objFour);

		// Overwrite object two with object one and check the same as before.
		Flip.shallowCopy(objOne, objTwo);
		Assert.assertEquals(objOne.getTestString(), objTwo.getTestString());
		Assert.assertNotEquals(objOne, objTwo);

		// Since we just copied the memory location of the object, we also want
		// to check that the old reference have the same value.
		Assert.assertEquals(objOne.getTestString(), objThree.getTestString());
	}

	/**
	 * Tests {@link Flip#sizeOf(Object)}.
	 */
	@Test
	public void testSizeOf() {
		// Test objects.
		WrappedObjectHeavyImpl heavyObj = new WrappedObjectHeavyImpl();
		WrappedObjectLightImpl lightObj = new WrappedObjectLightImpl();
		SupportObject objOne = new SupportObject("Object 1");
		objOne.setWrappedObject(heavyObj);
		SupportObject objTwo = new SupportObject("Object 256");
		objTwo.setWrappedObject(lightObj);

		// Checks that the objects have different sizes.
		Assert.assertNotEquals(Flip.sizeOf(lightObj), Flip.sizeOf(heavyObj));

		// The copy is shallow so objects should not be added to the size.
		Assert.assertEquals(Flip.sizeOf(objOne), Flip.sizeOf(objTwo));
	}

}
