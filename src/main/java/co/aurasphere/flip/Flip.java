package co.aurasphere.flip;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Unsafe;

public class Flip {

	private final static Logger LOG = LoggerFactory.getLogger(Flip.class);

	private static Unsafe unsafe;

	static {
		init();
	}

	private static void init() {
		Field theUnsafe;
		try {
			theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe) theUnsafe.get(null);
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static <T> T instantiateWithoutConstructor(Class<T> T) throws FlipException {
		try {
			return (T) unsafe.allocateInstance(T);
		} catch (InstantiationException e) {
			LOG.error("Error while instantiating class {} without constructor", e);
			throw new FlipException("Error while instantiating class {} without constructor", e);
		}
	}
	
	public static <T> T defineClass(){
		unsafe.define
	}

}
