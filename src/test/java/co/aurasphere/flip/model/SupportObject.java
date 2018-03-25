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
package co.aurasphere.flip.model;

import co.aurasphere.flip.TestFlip;

/**
 * Support object for {@link TestFlip}.
 * 
 * @author Donato Rimenti
 *
 */
@SuppressWarnings("unused")
public class SupportObject {

	// Bunch of random fields.
	/**
	 * A string.
	 */
	private String testString;

	/**
	 * The one.
	 */
	private int one;

	/**
	 * The two.
	 */
	private float two;

	/**
	 * The three.
	 */
	private double three;

	/**
	 * The four.
	 */
	private boolean four;

	/**
	 * The five.
	 */
	private char five;

	/**
	 * The six.
	 */
	private long six;

	/**
	 * The wrapped object.
	 */
	private WrappedObject wrappedObject;

	/**
	 * Instantiates a new support object.
	 */
	public SupportObject() {
		this.testString = "Starting value";
	}

	/**
	 * Instantiates a new SupportObject.
	 *
	 * @param testString
	 *            the {@link #testString}
	 */
	public SupportObject(String testString) {
		this.testString = testString;
	}

	/**
	 * Gets the {@link #testString}.
	 *
	 * @return the {@link #testString}
	 */
	public String getTestString() {
		return testString;
	}

	/**
	 * Sets the {@link #testString}.
	 *
	 * @param testString
	 *            the new {@link #testString}
	 */
	public void setTestString(String testString) {
		this.testString = testString;
	}

	/**
	 * Sets the {@link #wrappedObject}.
	 *
	 * @param wrappedObject
	 *            the new {@link #wrappedObject}
	 */
	public void setWrappedObject(WrappedObject wrappedObject) {
		this.wrappedObject = wrappedObject;
	}

}
