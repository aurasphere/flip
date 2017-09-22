package co.aurasphere.flip;

/**
 * Exception class for Flip library.
 * 
 * @author Donato Rimenti
 *
 */
public class FlipException extends Exception {

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
