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

import java.lang.reflect.Method;

/**
 * Flip informations about the underlying system environment.
 * 
 * @author Donato Rimenti
 *
 */
public class FlipSystemInformation {

	/**
	 * Underlying operative system.
	 */
	private static final String OS_ARCH = System.getProperty("os.arch");

	/**
	 * Sun JVM's bitness property.
	 */
	private static final String SUN_ARCH = System
			.getProperty("sun.arch.data.model");

	/**
	 * JVM is 64bit if true, 32bit if false.
	 */
	private static boolean is64Bit;

	/**
	 * JVM is HotSpot if true.
	 */
	public static boolean isHotspot;

	/** JVM uses compressed. */
	public static boolean compressedOopsEnabled;

	/**
	 * Management factory class.
	 */
	private static final String MANAGEMENT_FACTORY_CLASS = "java.lang.management.ManagementFactory";

	/**
	 * HotSpot class used to check if the underlying JVM is HotSpot.
	 */
	private static final String HOTSPOT_BEAN_CLASS = "com.sun.management.HotSpotDiagnosticMXBean";

	/**
	 * Loads all the system information.
	 */
	static {
		// Checks how many bits the JVM has.
		if (SUN_ARCH != null) {
			is64Bit = SUN_ARCH.contains("64");
		} else {
			if (OS_ARCH != null && OS_ARCH.contains("64")) {
				is64Bit = true;
			}
		}

		// Checks if we are dealing with an HotSpot JVM.
		try {
			Class<?> beanClazz = Class.forName(HOTSPOT_BEAN_CLASS);
			final Object hotSpotBean = Class.forName(MANAGEMENT_FACTORY_CLASS)
					.getMethod("getPlatformMXBean", Class.class)
					.invoke(null, beanClazz);
			if (hotSpotBean != null) {
				isHotspot = true;
			}

			// Checks if compressed oops are enabled only if the underlying JVM
			// is an HotSpot 64-bit since we only support HotSpot and we can't
			// have compressed oops in 32-bit JVMs.
			if (is64Bit && isHotspot) {
				Method getVMOptionMethod = beanClazz.getMethod("getVMOption",
						String.class);
				Object vmOption = getVMOptionMethod.invoke(hotSpotBean,
						"UseCompressedOops");
				compressedOopsEnabled = Boolean.parseBoolean(vmOption
						.getClass().getMethod("getValue").invoke(vmOption)
						.toString());
			}

		} catch (ReflectiveOperationException e) {
			// Just ignore this exception, all the flags have been set
			// already.
		}
	}

	/**
	 * Private constructor for utility class.
	 */
	private FlipSystemInformation() {
	}

	/**
	 * Checks if the underlying JVM is HotSpot.
	 *
	 * @return true, if the underlying JVM is HotSpot, false otherwise
	 */
	public static boolean isHotspot() {
		return isHotspot;
	}

	/**
	 * Checks if the underlying JVM is 32 bit.
	 *
	 * @return true, if the underlying JVM is 32 bit, false otherwise
	 */
	public static boolean is32Bit() {
		return !is64Bit;
	}

	/**
	 * Checks if the underlying JVM is 64 bit.
	 *
	 * @return true, if the underlying JVM is 64 bit, false otherwise
	 */
	public static boolean is64Bit() {
		return is64Bit;
	}

	/**
	 * Checks if the underlying JVM is using compressed OOPS.
	 *
	 * @return true, if the underlying JVM is using compressed OOPS, false
	 *         otherwise
	 */
	public static boolean compressedOopsEnabled() {
		return compressedOopsEnabled;
	}

	/**
	 * Checks if the underlying JVM is 64 bit and uses compressed OOPS.
	 *
	 * @return true,if the underlying JVM is 64 bit and uses compressed OOPS,
	 *         false otherwise
	 */
	public static boolean is64BitWithCompressedOopsEnabled() {
		return is64Bit() && compressedOopsEnabled();
	}

}