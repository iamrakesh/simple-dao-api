package com.rakesh.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Loads property files from classpath.
 *
 * @author Elli Albek
 */
public class PropertyLoader {

	public static Properties load(final Class<?> caller, final String resourceName) throws IOException {
		InputStream in = caller.getClassLoader().getResourceAsStream(resourceName);
		if (in == null)
			throw new FileNotFoundException("Property file '" + resourceName + "' does not exist in classpath");
		try {
			Properties p = new Properties();
			p.load(in);
			return p;
		} finally {
			in.close();
		}
	}

	public static Properties load(final File f) throws IOException {
		if (f.exists() == false)
			throw new FileNotFoundException("Property file '" + f.getAbsolutePath() + "' does not exist");
		InputStream in = new FileInputStream(f);
		try {
			Properties p = new Properties();
			p.load(in);
			return p;
		} finally {
			in.close();
		}
	}

	/**
	 * Reads the content of the file as one big string.
	 */
	public static final String loadFile(final Class<?> caller) throws IOException {
		String resourceName = caller.getSimpleName() + ".properties";
		return readFile(caller, resourceName);
	}

	/**
	 * Reads a text file from the class folder
	 */
	public static String readFile(final Class<?> caller, final String resourceName) throws IOException {
		InputStream in = caller.getResourceAsStream(resourceName);
		if (in != null) {
			return readStringAndClose(in);
		}
		throw new IOException("Cannot find properties file '" + resourceName + "'");
	}

	/**
	 * Returns the content of the input stream as a string, and close the input
	 * stream.
	 */
	private static String readStringAndClose(final InputStream in) throws IOException {
		try {
			InputStreamReader r = new InputStreamReader(in, "UTF-8");
			StringWriter w = new StringWriter();
			char[] buf = new char[1024];
			int read = -1;
			while ((read = r.read(buf)) >= 0) {
				w.write(buf, 0, read);
			}
			return w.toString();
		} finally {
			in.close();
		}
	}
}
