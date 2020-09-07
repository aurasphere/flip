[![Donate](https://img.shields.io/badge/Donate-PayPal-orange.svg)](https://www.paypal.com/donate/?cmd=_donations&business=8UK2BZP2K8NSS)

# flip
A Java library for doing stuff you are not supposed to.

The library is heavily based on the <code>sun.misc.Unsafe</code> class and thus is usage in production environments is discouraged because of portability issues.

This library includes the following methods:

 - **getObjectAddress**: returns the memory address of an object instance
 - **getClassAddress**: returns the memory address of a Class object from an object instance
 - **sizeOf**: computes the shallow size of an object
 - **readObject**: reads an object from a memory address
 - **shallowCopy**: copies an object to a new memory location or overwriting another existing one
 - **dynamicClassDefinition**: dynamically defines a new class by compiling a source code string passed as argument. The new class can be used immediately through reflection
 - **getUnsafe**: exposes the <code>sun.misc.Unsafe</code> object
