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

/**
 * Exception class for the Flip library. It extends {@link RuntimeException}
 * since all exception thrown by Flip are related to the runtime environment,
 * such as the JVM or platforms like Android and thus it's reasonable to expect
 * that the client code could not recover from such problems.
 * 
 * @author Donato Rimenti
 *
 */
public class FlipException extends RuntimeException {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new FlipException.
	 *
	 * @param message
	 *            the message.
	 * @param cause
	 *            the cause.
	 */
	public FlipException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new FlipException.
	 *
	 * @param message
	 *            the message.
	 */
	public FlipException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new FlipException.
	 *
	 * @param cause
	 *            the cause.
	 */
	public FlipException(Throwable cause) {
		super(cause);
	}

}
