package co.aurasphere.flip;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

/**
 * Main class of Flip library.
 * 
 * @author Donato Rimenti
 *
 */
@SuppressWarnings({ "restriction", "unchecked" })
public class Flip {

	/**
	 * Logger.
	 */
	private final static Logger LOG = LoggerFactory.getLogger(Flip.class);

	/**
	 * Unsafe used for Flip operations.
	 */
	private static Unsafe unsafe;

	/**
	 * Static initializer.
	 */
	static {
		init();
	}

	/**
	 * Initializes Flip by loading the {@link #unsafe}.
	 */
	private static void init() {
		Field theUnsafe;
		try {
			theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe) theUnsafe.get(null);
		} catch (ReflectiveOperationException e) {
			LOG.error("Error while instantiating class {} without constructor", e);
		}
	}

	/**
	 * Instantiates a class without calling any constructor.
	 * 
	 * @param T
	 *            the class to instantiate
	 * @return an instance of that class
	 * @throws FlipException
	 */
	public static <T> T instantiateWithoutConstructor(Class<T> T) throws FlipException {
		try {
			return (T) unsafe.allocateInstance(T);
		} catch (InstantiationException e) {
			LOG.error("Error while instantiating class {} without constructor", e);
			throw new FlipException("Error while instantiating class {} without constructor", e);
		}
	}

//	public static <T, E> T injectConstructor(Class<T> T, Constructor<E> constructor) {
//		Constructor<T> silentConstructor = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(T,
//				constructor);
//		silentConstructor.setAccessible(true);
//		T instance = silentConstructor.newInstance();
//		return null;
//	}

}
