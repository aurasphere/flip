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

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/**
 * Java file manager used by
 * {@link Flip#dynamicClassDefinition(String, String, String)}.
 * 
 * @author Donato Rimenti
 *
 */
class FlipJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	/**
	 * Java file object managed by this file manager.
	 */
	private final FlipJavaFileObject javaFile;

	/**
	 * Instantiates a new FlipJavaFileManager.
	 *
	 * @param fullClassName
	 *            the full class name
	 * @param classSource
	 *            the class source
	 */
	FlipJavaFileManager(String fullClassName, String classSource) {
		super(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null,
				null, null));
		this.javaFile = new FlipJavaFileObject(URI.create(fullClassName
				+ ".java"), classSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools
	 * .JavaFileManager.Location, java.lang.String,
	 * javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, JavaFileObject.Kind kind, FileObject sibling) {
		return javaFile;
	}

	/**
	 * Gets the {@link #javaFile}.
	 *
	 * @return the {@link #javaFile}
	 */
	public FlipJavaFileObject getJavaFile() {
		return javaFile;
	}

	/**
	 * Java File Object used by
	 * {@link Flip#dynamicClassDefinition(String, String, String)}.
	 * 
	 * @author Donato Rimenti
	 *
	 */
	class FlipJavaFileObject extends SimpleJavaFileObject {

		/**
		 * The class source.
		 */
		private final String classSource;

		/**
		 * Output stream of this file.
		 */
		private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		/**
		 * Instantiates a new FlipJavaFileObject.
		 *
		 * @param uri
		 *            the uri
		 * @param classSource
		 *            the {@link #classSource}
		 */
		FlipJavaFileObject(URI uri, String classSource) {
			super(uri, Kind.SOURCE);
			this.classSource = classSource;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
		 */
		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return classSource;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.tools.SimpleJavaFileObject#openOutputStream()
		 */
		@Override
		public ByteArrayOutputStream openOutputStream() {
			return byteArrayOutputStream;
		}
	}
}