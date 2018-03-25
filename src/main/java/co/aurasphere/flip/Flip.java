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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;

import javax.tools.ToolProvider;

import sun.misc.Unsafe;
import co.aurasphere.flip.FlipJavaFileManager.FlipJavaFileObject;

/**
 * Main class of the Flip library.
 * 
 * @author Donato Rimenti
 * @see <a
 *      href="http://mishadoff.com/blog/java-magic-part-4-sun-dot-misc-dot-unsafe/">Inspiration
 *      for this library and sun.misc.Unsafe tutorial</a>
 * @see <a
 *      href="https://zeroturnaround.com/rebellabs/dangerous-code-how-to-be-unsafe-with-java-classes-objects-in-memory">Zero
 *      Turnaround Unsafe tutorial</a>
 *
 */
public class Flip {

	/**
	 * Unsafe used for Flip operations.
	 */
	private static Unsafe unsafe;

	/**
	 * Initializes Flip by loading the {@link #unsafe}.
	 */
	static {
		Field theUnsafe;
		try {
			theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe) theUnsafe.get(null);
		} catch (ReflectiveOperationException e) {
			throw new FlipException(
					"Error while getting the unsafe class during Flip initialization",
					e);
		}
	}

	/**
	 * Private constructor for utility class.
	 */
	private Flip() {
	}

	/**
	 * Returns the address of a class from an object. HotSpot object addresses
	 * are defined as following:
	 * 
	 * <pre>
	 * For 32 bit JVM:
	 * 	_mark	: 4 byte constant
	 * 	_klass	: 4 byte pointer to class 
	 * 
	 * For 64 bit JVM:
	 * 	_mark	: 8 byte constant
	 * 	_klass	: 8 byte pointer to class
	 * 
	 * For 64 bit JVM with compressed-oops:
	 * 	_mark	: 8 byte constant
	 * 	_klass	: 4 byte pointer to class
	 * </pre>
	 * 
	 * @param object
	 *            the object whose class address needs to be found
	 * @return a class object address
	 * @see <a
	 *      href="https://zeroturnaround.com/rebellabs/dangerous-code-how-to-be-unsafe-with-java-classes-objects-in-memory/3/">Source</a>
	 */
	public static long getClassAddress(Object object) {
		if (!FlipSystemInformation.isHotspot()) {
			throw new FlipException("Operation only supported on HotSpot JVMs.");
		}
		// 32-bit
		if (FlipSystemInformation.is32Bit()) {
			return unsafe.getInt(object, 4L);
		}
		// 64-bit no oops
		if (!FlipSystemInformation.compressedOopsEnabled()) {
			return unsafe.getLong(object, 8L);
		}
		// 64-bit with oops
		return unsafe.getInt(object, 8L);
	}

	/**
	 * Returns the address of an object.
	 * 
	 * @param object
	 *            the object whose address needs to be found
	 * @return an object address
	 * @see <a
	 *      href="https://zeroturnaround.com/rebellabs/dangerous-code-how-to-be-unsafe-with-java-classes-objects-in-memory/4/">Source</a>
	 */
	public static long getObjectAddress(Object object) {
		if (!FlipSystemInformation.isHotspot()) {
			throw new FlipException("Operation only supported on HotSpot JVMs.");
		}
		Object[] helperArray = new Object[] { object };
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);

		// 32-bit addresses are 4 bytes.
		if (FlipSystemInformation.is32Bit()) {
			return unsafe.getInt(helperArray, baseOffset);
		}
		// 64-bit addresses are 8 bytes.
		return unsafe.getLong(helperArray, baseOffset);
	}

	/**
	 * Returns the shallow size of an object in a C-style fashion. The result is
	 * expressed as number of bytes representing the maximum offset of a field
	 * plus the padding.
	 * 
	 * @param object
	 *            the object whose size needs to be computed
	 * @return the shallow size of the object
	 */
	public static long sizeOf(Object object) {
		// First step: go through all non-static fields of each superclass
		HashSet<Field> fields = new HashSet<Field>();
		Class<?> klass = object.getClass();
		while (klass != Object.class) {
			for (Field field : klass.getDeclaredFields()) {
				if ((field.getModifiers() & Modifier.STATIC) == 0) {
					fields.add(field);
				}
			}
			klass = klass.getSuperclass();
		}

		// Gets the max offset.
		long maxSize = 0;
		for (Field field : fields) {
			long offset = unsafe.objectFieldOffset(field);
			if (offset > maxSize) {
				maxSize = offset;
			}
		}

		// Adds the padding and returns.
		return ((maxSize / 8) + 1) * 8;
	}

	/**
	 * Reads an object from a memory location and returns it.
	 * 
	 * @param address
	 *            the address of the memory to read
	 * @return the object at that memory location
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(long address) {
		Object[] array = new Object[] { null };
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		unsafe.putLong(array, baseOffset, address);
		return (T) array[0];
	}

	/**
	 * Creates a shallow copy of an object and returns it.
	 * 
	 * @param objectToCopy
	 *            the object to copy
	 * @return the new object
	 */
	public static <T> T shallowCopy(T objectToCopy) {
		// Gets the address info.
		long size = sizeOf(objectToCopy);
		long start = getObjectAddress(objectToCopy);
		long address = unsafe.allocateMemory(size);

		// Copies the object.
		unsafe.copyMemory(start, address, size);
		return readObject(address);
	}

	/**
	 * Creates a shallow copy of an object by replacing the memory occupied by
	 * another object of the same type.
	 * 
	 * @param objectToCopy
	 *            the object to copy
	 * @param objectToOverwrite
	 *            the object to replace
	 * @return the new object
	 */
	public static <T> T shallowCopy(T objectToCopy, T objectToOverwrite) {
		unsafe.copyMemory(objectToCopy, 0L, null,
				getObjectAddress(objectToOverwrite), sizeOf(objectToOverwrite));
		return objectToCopy;
	}

	/**
	 * Creates a new class dynamically by compiling a String of source code.
	 * 
	 * @param classPackage
	 *            the new class package
	 * @param className
	 *            the new class name
	 * @param classSource
	 *            the new class source
	 * @return a compiled class
	 */
	public static Class<?> dynamicClassDefinition(String classPackage,
			String className, String classSource) {
		String fullClassName = classPackage.replace('.', '/') + "/" + className;

		// Compiles the source.
		FlipJavaFileManager javaFileManager = new FlipJavaFileManager(
				fullClassName, classSource);
		FlipJavaFileObject javaFile = javaFileManager.getJavaFile();
		ToolProvider
				.getSystemJavaCompiler()
				.getTask(null, javaFileManager, null, null, null,
						Collections.singletonList(javaFile)).call();

		// Instantiates the new class.
		byte[] bytes = javaFile.openOutputStream().toByteArray();
		return unsafe.defineClass(fullClassName, bytes, 0, bytes.length,
				Flip.class.getClassLoader(), Flip.class.getProtectionDomain());

	}

	/**
	 * Gets the {@link #unsafe}.
	 * 
	 * @return the {@link #unsafe}
	 */
	public static Unsafe getUnsafe() {
		return unsafe;
	}
}
